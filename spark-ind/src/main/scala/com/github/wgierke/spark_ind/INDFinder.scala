package de.hpi.spark_tutorial

import org.apache.spark.sql._
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.DecisionTreeClassificationModel
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.log4j.Logger
import org.apache.log4j.Level
import java.io.File

import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder

// A Scala case class; works out of the box as Dataset type using Spark's implicit encoders
case class Person(name:String, surname:String, age:Int)

// A non-case class; requires an encoder to work as Dataset type
class Pet(var name:String, var age:Int) {
  override def toString = s"Pet(name=$name, age=$age)"
}

object SparkIND extends App {

  override def main(args: Array[String]): Unit = {

    // Turn off logging
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    //------------------------------------------------------------------------------------------------------------------
    // Lamda basics (for Scala)
    //------------------------------------------------------------------------------------------------------------------

//    //sparkSession uses user defined functions to transform data, lets first look at how functions are defined in scala:
//    val smallListOfNumbers = List(1, 2, 3, 4, 5)
//
//    // A Scala map function from int to double
//    def squareAndAdd(i: Int): Double = {
//      i * 2 + 0.5
//    }
//
//    // A Scala map function defined in-line (without curly brackets)
//    def squareAndAdd2(i: Int): Double = i * 2 + 0.5
//
//    // A Scala map function inferring the return type
//    def squareAndAdd3(i: Int) = i * 2 + 0.5
//
//    // An anonymous Scala map function assigned to a variable
//    val squareAndAddFunction = (i: Int) => i * 2 + 0.5
//
//    // Different variants to apply the same function
//    println(smallListOfNumbers.map(squareAndAdd))
//    println(smallListOfNumbers.map(squareAndAdd2))
//    println(smallListOfNumbers.map(squareAndAdd3))
//    println(smallListOfNumbers.map(squareAndAddFunction))
//    println(smallListOfNumbers.map(i => i * 2 + 0.5)) // anonymous function; compiler can infers types
//    println(smallListOfNumbers.map(_ * 2 + 0.5)) // syntactic sugar: '_' maps to first (second, third, ...) parameter

    //------------------------------------------------------------------------------------------------------------------
    // Setting up a Spark Session
    //------------------------------------------------------------------------------------------------------------------

    // Create a SparkSession to work with Spark
    val sparkBuilder = SparkSession
      .builder()
      .appName("fINDer")
      .master("local[4]") // local, with 4 worker cores
    val sparkSession = sparkBuilder.getOrCreate()

    // Set the default number of shuffle partitions to 5 (default is 200, which is too high for local deployment)
    sparkSession.conf.set("sparkSession.sql.shuffle.partitions", "5") //

    // Importing implicit encoders for standard library classes and tuples that are used as Dataset types
//    import sparkSession.implicits._

//    //------------------------------------------------------------------------------------------------------------------
//    // Loading data
//    //------------------------------------------------------------------------------------------------------------------
//
//    // Create a Dataset programmatically
//    val numbers = sparkSession.createDataset((0 until 100).toList)
//
//    // Read a Dataset from a file
//    val employees = sparkSession.read
//      .option("inferSchema", "true")
//      .option("header", "true")
//      .csv("src/main/resources/employees.csv") // also text, json, jdbc, parquet
//      .as[(String, Int, Double, String)]
//
//    //------------------------------------------------------------------------------------------------------------------
//    // Basic transformations
//    //------------------------------------------------------------------------------------------------------------------
//
//    // Basic transformations on datasets return new datasets
//    val mapped = numbers.map(i => "This is a number: " + i)
//    val filtered = mapped.filter(s => s.contains("1"))
//    val sorted = filtered.sort()
//    List(numbers, mapped, filtered, sorted).foreach(dataset => println(dataset.getClass))
//    sorted.show()
//
//    // Basic terminal operations
//    val collected = filtered.collect() // collects the entire dataset to the driver process
//    val reduced = filtered.reduce((s1, s2) => s1 + "," + s2) // reduces all values successively to one
//    filtered.foreach(s => println(s)) // performs an action for each element (take care where the action is evaluated!)
//    List(collected, reduced).foreach(result => println(result.getClass))
//
//    // DataFrame and Dataset
//    val untypedDF = numbers.toDF() // DS to DF
//    val stringTypedDS = untypedDF.map(r => r.get(0).toString) // DF to DS via map
//    val integerTypedDS = untypedDF.as[Int] // DF to DS via as() function that cast columns to a concrete types
//    List(untypedDF, stringTypedDS, integerTypedDS).foreach(result => println(result.getClass))
//
//    // Mapping to tuples
//    numbers
//      .map(i => (i, "nonce", 3.1415, true))
//      .take(10)
//      .foreach(println(_))
//
//    // SQL on DataFrames
//    employees.createOrReplaceTempView("employee") // make this dataframe visible as a table
//    val sqlResult = sparkSession.sql("SELECT * FROM employee WHERE Age > 95") // perform an sql query on the table
//
//    import org.apache.sparkSession.sql.functions._
//
//    sqlResult // DF
//      .as[(String, Int, Double, String)] // DS
//      .sort(desc("Salary")) // desc() is a standard function from the sparkSession.sql.functions package
//      .head(10)
//      .foreach(println(_))
//
//    // Grouping and aggregation for Datasets
//    val topEarners = employees
//      .groupByKey { case (name, age, salary, company) => company }
//      .mapGroups { case (key, iterator) =>
//        val topEarner = iterator.toList.maxBy(t => t._3) // could be problematic: Why?
//        (key, topEarner._1, topEarner._3)
//      }
//      .sort(desc("_3"))
//    topEarners.collect().foreach(t => println(t._1 + "'s top earner is " + t._2 + " with salary " + t._3))
//
    //------------------------------------------------------------------------------------------------------------------
    // Homework
    //------------------------------------------------------------------------------------------------------------------

    val files = List(
//      "src/main/resources/TPCH/tpch_customer.csv"
//      , "src/main/resources/TPCH/tpch_lineitem.csv"
       "src/main/resources/TPCH/tpch_nation.csv"
//      , "src/main/resources/TPCH/tpch_orders.csv"
//      , "src/main/resources/TPCH/tpch_part.csv"
//      , "src/main/resources/TPCH/tpch_region.csv"
      , "src/main/resources/TPCH/tpch_supplier.csv"
    )

    import sparkSession.implicits._

    // Homework
    val tables = files.map(file => {
      sparkSession.read
        .option("inferSchema", "true")
        .option("header", "true")
        .option("delimiter", ";")
        .csv(file)
    })

    tables.foreach(_.printSchema)

    //    implicit val mapEncoder: Encoder[Map[String, String]] = org.apache.spark.sql.Encoders.kryo[Map[String, String]]
    // Primitive types and case classes can be also defined as
//     implicit val stringIntMapEncoder: Encoder[Map[String, String]] = ExpressionEncoder()
//     implicit val stringIntMapEncoder2: Encoder[IndexedSeq[String]] = ExpressionEncoder()
//     implicit val stringIntMapEncoder3: Encoder[Dataset[IndexedSeq[String]]] = ExpressionEncoder()
//    implicit val foo: Encoder[Dataset[IndexedSeq[String]]] = org.apache.spark.sql.Encoders.kryo[Dataset[IndexedSeq[String]]]
//    implicit val foo2: Encoder[Dataset[Map[String, List[String]]]] = ExpressionEncoder()
    //    implicit val foo3: Encoder[Map[String, List[String]]] = org.apache.spark.sql.Encoders.kryo[Map[String, List[String]]]
//        implicit val foo5: Encoder[Set[String]] = ExpressionEncoder()

//    implicit val foo4: Encoder[Map[String, List[String]]] = ExpressionEncoder()
//
//    implicit val setEncoder: Encoder[Set[String]] = Encoders.kryo[Set[String]]
//    implicit val foo1: Encoder[Dataset[Map[String, List[String]]]] = org.apache.spark.sql.Encoders.kryo[Dataset[Map[String, List[String]]]]

    import scala.reflect.ClassTag
    implicit def kryoEncoder[A](implicit ct: ClassTag[A]) =
      org.apache.spark.sql.Encoders.kryo[A](ct)


    val tableCells = tables.map(table => {
      table.map(row => {
        row.schema.fieldNames.zipWithIndex.map{
          case (columnName, idx) => (row.get(idx).toString.trim, List(columnName))
        }
      })
    })

    val unifiedCells = tableCells.reduce((a, b) => a.union(b))

    val groupedCells = unifiedCells.flatMap(cells => cells.groupBy(_._1).mapValues(x => x.foldLeft(List[String]())((a, b) => a ++ b._2)))

    val aggregatedCells = groupedCells.groupByKey(_._1).reduceGroups((a, b) => (a._1, a._2 ++ b._2)).map(x => (x._2._1, x._2._2.distinct))

    val attributeSets = aggregatedCells.filter(_._2.size > 1)

    attributeSets.foreach(x => println(x))


//    groupedCells.map(_.map(cells => cells.reduce(x => x)))

  }

}
