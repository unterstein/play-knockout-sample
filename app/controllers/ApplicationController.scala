package controllers

import java.lang.reflect.Type
import java.util.Date
import javax.inject.Inject

import com.google.gson.{JsonElement, JsonSerializationContext, JsonSerializer, GsonBuilder}
import elastic.models.GuestPost
import elastic.services.ElasticProvider
import elasticplugin.ElasticPlugin
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import scala.collection.JavaConversions._

class ApplicationController @Inject()(val plugin: ElasticPlugin, val messagesApi: MessagesApi) extends Controller with I18nSupport {


  def list = Action {
    implicit request =>
      Ok(views.html.index(allPosts, emptyForm))
  }

  def create = Action {
    implicit request =>
      entryForm.bindFromRequest().fold(
        formWithErrors => Ok(views.html.index(allPosts, formWithErrors)),
        value => {
          val newPost = new GuestPost()
          newPost.author = value.author.getOrElse("")
          newPost.headline = value.headline.getOrElse("")
          newPost.text = value.text.getOrElse("")
          newPost.postedDate = new Date()
          ElasticProvider.get().guestPostRepository.save(newPost)
          Ok(views.html.index(allPosts, emptyForm))
        }
      )
  }

  def allPosts = ListModel(ElasticProvider.get().guestPostRepository.findAll().toList)

  def emptyForm = entryForm.fill(NewEntry(None, None, None))

  val entryForm: Form[NewEntry] = Form(
    mapping(
      "headline" -> optional(text),
      "text" -> optional(text),
      "author" -> optional(text)
    )(NewEntry.apply)(NewEntry.unapply)
  )
}

/**
 * Wrapper for a list of posts
 */
case class ListModel(posts: List[GuestPost])

case class NewEntry(headline: Option[String], text: Option[String], author: Option[String])


/**
 * Just a simple gson json wrapper
 */
object Json {

  import scala.collection.JavaConverters._

  private val gson = new GsonBuilder().registerTypeAdapter(classOf[List[Any]], new ListSerializer()).create()

  def toJson(obj: Object): String = gson.toJson(obj)

  def fromJson[T](json: String, clazz: Class[T]): T = gson.fromJson(json, clazz)

  class ListSerializer extends JsonSerializer[List[Any]] {

    override def serialize(src: List[Any], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      gson.toJsonTree(src.asJava)
    }
  }
}