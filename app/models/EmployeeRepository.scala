package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EmployeeRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._

  private class EmployeeTable(tag: Tag) extends Table[Employee](tag, "people") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] = column[String]("name")

    def age: Rep[Int] = column[Int]("age")

    def * : ProvenShape[Employee] = (id, name, age) <> ((Employee.apply _).tupled, Employee.unapply)
  }

  private val people = TableQuery[EmployeeTable]

  def create(name: String, age: Int): Future[Employee] = db.run {
    (people.map(p => (p.name, p.age))
      returning people.map(_.id)
      into ((nameAge, id) => Employee(id, nameAge._1, nameAge._2))
    ) += (name, age)
  }

  def list(): Future[Seq[Employee]] = db.run {
    people.result
  }
}
