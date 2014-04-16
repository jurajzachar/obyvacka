package controllers

import play.api._
import play.api.mvc._
import common.AppEnv
import java.io.FileOutputStream
import scala.io.Source
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import scala.util.Marshal
import java.io.FileInputStream
import java.io.File

object Application extends Controller {

  val logger: Logger = LoggerFactory.getLogger("Obyvacka_Application")

  var visitors = 0

  protected val env = new AppEnv(Play.unsafeApplication.configuration)

  def show(page: String) = {

    import views.html._

    Action {
      implicit request =>
        {
           visitors match {
              case 0 => readCounter
              case _ =>
            }
           visitors += 1
           dumpCounter
           
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

  def dumpCounter = {
    try {
      val out = new FileOutputStream(env.metaFile)
      out.write(Marshal.dump(visitors))
      out.close()
    } catch {
      case e: Exception => logger.error("Eeek!", e)
    }
  }

  def readCounter = {
    try {
      if(!env.metaFile.exists()) dumpCounter
      val in = new FileInputStream(env.metaFile)
      val bytes = Stream.continually(in.read).takeWhile(-1 !=).map(_.toByte).toArray
      visitors = Marshal.load[Int](bytes)
    }
    catch {
      case e: Exception => logger.error("Eeek!", e)
    }
  }

}