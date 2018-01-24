package com.github.wgierke.finder

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql._

import scala.collection.mutable.ListBuffer

object fINDer extends App {

  override def main(args: Array[String]): Unit = {

    // Turn off logging
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    var cores = 4
    var file_path = "TPCH"

    for(i <- 0 until args.length - 1) {
      if (args(i) == "--path") {
        file_path = args(i + 1)
      }
      if (args(i) == "--cores") {
        cores = args(i + 1).toInt
      }
    }

    val sparkBuilder = SparkSession
      .builder()
      .appName("fINDer")
      .master("local[" + cores + "]") // local, with 4 worker cores per default

    val sparkSession = sparkBuilder.getOrCreate()
    import sparkSession.implicits._

    println($"Solving $file_path on $cores cores")

    val files = List(
      file_path + "/tpch_customer.csv"
      , file_path + "/tpch_lineitem.csv"
      , file_path + "/tpch_nation.csv"
      , file_path + "/tpch_orders.csv"
      , file_path + "/tpch_part.csv"
      , file_path + "/tpch_region.csv"
      , file_path + "/tpch_supplier.csv"
    )

    val tables = files.map(file => {
      sparkSession.read
        .option("inferSchema", "true")
        .option("header", "true")
        .option("delimiter", ";")
        .csv(file)
    })

    // Convert each value to (<value>, <column>)
    val tableCells = tables.map(table => {
      table.map(row => {
        row.schema.fieldNames.zipWithIndex.map{
          case (columnName, idx) => (row.get(idx).toString.trim, ListBuffer[String](columnName))
        }
      })
    })

    // Merge all <value, column) tuple into one big RDD
    val unifiedCells = tableCells.reduce((a, b) => a.union(b)).flatMap(x => x).rdd

    // Merge all <column>s for each <value> and discard <value>
    val attributeSets = unifiedCells.reduceByKey((a, b) => a ++= b).map(_._2.distinct)

    // Fan out attribute sets to all subset combinations (<column>, <all-other-columns>)
    val inclusionLists = attributeSets.flatMap(attrSet => attrSet.map(col => (col, attrSet.filter(!_.equals(col)))))

    // Group each <column> and intersect its <all-other-columns>. Discard resulting empty sets
    val aggregatedINDs = inclusionLists.reduceByKey((a, b) => a.intersect(b)).filter(_._2.nonEmpty)

    // Convert into fancy string
    val strings = aggregatedINDs.collect().map(ind => $"""${ind._1} < ${ind._2.mkString(", ")}""".toString()).sorted

    strings.foreach(println)
  }
}
