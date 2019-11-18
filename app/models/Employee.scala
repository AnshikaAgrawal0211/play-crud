package models

import play.api.libs.json._

case class Employee(id: Long, name: String, age: Int)

object Employee {
  implicit val employeeFormat: OFormat[Employee] = Json.format[Employee]
}
