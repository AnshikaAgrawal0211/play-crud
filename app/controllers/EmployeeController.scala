package controllers

import form.EmployeeFormMappingData
import javax.inject._
import models._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class EmployeeController @Inject()(repo: EmployeeRepository,
                                   employee:EmployeeFormMappingData,
                                   cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def index:Action[AnyContent] = Action { implicit request =>
    Ok(views.html.index(employee.employeeForm))
  }

  def addPerson: Action[AnyContent] = Action.async { implicit request =>
    employee.employeeForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      person => {
        repo.create(person.name, person.age).map { _ =>
          Redirect(routes.EmployeeController.index).flashing("success" -> "user.created")
        }
      }
    )
  }

  def getPersons: Action[AnyContent] = Action.async { implicit request =>
    repo.list().map { people =>
      Ok(Json.toJson(people))
    }
  }
}
