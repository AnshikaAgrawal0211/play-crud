package form

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._

case class EmployeeData(name: String, age: Int)

class EmployeeFormMappingData {
  val employeeForm: Form[EmployeeData] = Form {
    mapping(
      "name" -> nonEmptyText,
      "age" -> number.verifying(min(0), max(140))
    )(EmployeeData.apply)(EmployeeData.unapply)
  }
}

