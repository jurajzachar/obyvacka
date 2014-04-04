package controllers

import play.api._
import play.api.mvc._
import common.AppEnv

object Application extends Controller {

  var visitors: Integer = 0

  protected val env = new AppEnv(Play.unsafeApplication.configuration)

  def show(page: String) = {

    import views.html._

    Action {
      implicit request =>
        {
          visitors += 1
          page match {
            case "index" => Ok(views.html.index())
            case "about" => Ok(about())
            case "shut-up-manifesto" => Ok(views.html.shutUpManifesto())
            case _ => NotFound(views.html.error404())
          }
        }
    }
  }

  def index() = Action {

    Ok(views.html.index())

  }

  def picture(name: String) = Action {
    Ok.sendFile(new java.io.File(name)) // the name should contains the image extensions
  }

  def reviews() = Action {
    implicit request =>
      Ok(views.html.reviews.list(env.reviewService.reviewList(Some(env.dataReviewDirectory))))
  }

  def display(slug: String) = Action {

    implicit request =>
      env.reviewService.findReviewBySlug(slug, Some(env.dataReviewDirectory)) match {
        case Some(p) => Ok(views.html.reviews.review(p))
        case None => NotFound(views.html.error404())
      }
  }

}