package controllers

import java.util.Date
import javax.inject.Inject

import elastic.models.GuestPost
import elastic.services.ElasticProvider
import elasticplugin.ElasticPlugin
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class ApplicationController @Inject()(val plugin: ElasticPlugin, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index = Action {
    implicit request =>
      val newPost = new GuestPost
      newPost.headline = "Test Post #" + (ElasticProvider.get().guestPostRepository.count() + 1)
      newPost.author = "Johannes"
      newPost.postedDate = new Date()
      newPost.text = "Hi there, this works!"
      ElasticProvider.get().guestPostRepository.save(newPost)

      Ok(views.html.index("Posts in your database: " + ElasticProvider.get().guestPostRepository.count()))
  }

}
