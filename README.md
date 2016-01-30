# Step by step introduction of the usage from knockout js and the playframework

While working at my regular job with the playframework providing a RESTful API (and some other magic) for an Angular frontend, i do not want to do this big stack when i am developing on my own. Angular is sometimes ... a little bit too much, knockout however fits smoothly in the whole play development process.

Yes, it is reasonable to separate the frontend application and the play middleware (separation of development, deployment, ...), but play is a fullstack framework and i therefore we use it full stack in this sample. And its awesome by the way ;-).

In this repository i will show the needed 6 steps to kickstart the project and having some fun with knockout js and the playframework. All important steps are mentioned in this README.

You can also have a look the commits (commit messages equals headlines in README), where i did exactly the same like what i described in this file, see https://github.com/unterstein/play-knockout-sample/commits/master .

To start this example app, just clone this repo, browse to this folder in your shell and do

```
activator -J-Xmx2048m run
```

and open http://localhost:9000 in your favourite browser :-)

By the fact that we use elasticsearch in embedded mode, we need a little bit of extra heap space, sorry for that.


# Step 1 - Init the project

The initial step using "activator new -> play-scala" template and writing the skeleton of this README.


# Step 2 - Add dependencies

Added the following dependencies:

```
  // persistence
  "com.github.unterstein" %% "play-elasticplugin" % "0.2.0",
  // utility
  "com.google.code.gson" % "gson" % "2.4",
  // webjars
  "org.webjars" %% "webjars-play" % "2.4.0-2",
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.webjars" % "bootswatch-flatly" % "3.3.5+4",
  "org.webjars" % "jquery" % "2.1.4",
  "org.webjars" % "font-awesome" % "4.5.0"
  // webjars -> knockout
  "org.webjars" % "knockout" % "3.4.0",
  "org.webjars.bower" % "knockout-mapping" % "2.4.1",
  "org.webjars.bower" % "knockout-validation" % "2.0.3" exclude("org.webjars.bower", "knockout")
```

I decided to add a bootstrap theme from bootswatch and add font-awesome. This dependencies are note required, but nice to have :-). As described in the intro, we use elasticsearch as data store with the https://github.com/unterstein/play-elasticplugin for easy usage. I publish this repo on my github page, therefore we need to add the following resolver:

```
resolvers += "unterstein.github.io" at "http://unterstein.github.io/repo"
```


# Step 3 - Init and use webjars and add some styling

In step 2 we added a lot of webjars stuff to our build.sbt, this stuff must be included in our application now.

To address the webjars assets through http, the following line must be added to the routes file:

```
GET        /webjars/*file        controllers.WebJarAssets.at(file)
```


The main work was done in the main.scala.html:

```
    <link rel="stylesheet" media="screen" href="@routes.WebJarAssets.at(WebJarAssets.locate("bootswatch-flatly", "bootstrap.min.css"))">
    <link rel="stylesheet" media="screen" href="@routes.WebJarAssets.at(WebJarAssets.locate("font-awesome", "font-awesome.css"))">
    <link rel="stylesheet" media="screen" href="@routes.Assets.at("stylesheets/main.css")">

    ... some content here ...

    <script src="@routes.WebJarAssets.at(WebJarAssets.locate("jquery.min.js"))"></script>
    <script src="@routes.WebJarAssets.at(WebJarAssets.locate("bootswatch-flatly", "bootstrap.min.js"))"></script>
    <script src="@routes.WebJarAssets.at(WebJarAssets.locate("knockout", "knockout.js"))"></script>
    <script src="@routes.WebJarAssets.at(WebJarAssets.locate("knockout-mapping", "knockout.mapping.js"))"></script>
    <script src="@routes.WebJarAssets.at(WebJarAssets.locate("knockout-validation", "knockout.validation.min.js"))"></script>
    <script src="@routes.Assets.at("javascripts/main.js")"></script>
```


With WebJarAssets.locate("projectName", "fileName") the according file can be addressed which is inside the binary dependency to the relevant webjars artifact.
By this mechanism can be differed between the boostrap.css in the bootstrap artifact and the bootswatch-flatly artifact, which is really cool.


# Step 4 - Add (sample) data model and repositories (used spring-data-elasticsearch through my play-elasticplugin)

I decided to implement a simple guestbook. For the younger people: A guestbook was part of websites in the late 90s where visitors can post their stupid messages. Whatever ..

I started by adding the needed classes for the play-elasticplugin:

```
  └── elastic
     ├── models
     |   └──> GuestPost.java
     ├── repositories
     |   └──> GuestPostRepository.java
     └── services
         └──> ElasticProvider.java
```

And i added the ElasticPlugin as @Inject to the ApplicationController:

```
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
```

With each request to http://localhost:9000 a new GuestPost will be created and stored. After a few reloads i got

```
Posts in your database: 5
```

and by querying ElasticSearch directly through http://localhost:9200/guestposts/_search?pretty=true i got

```
{
  "took" : 22,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "failed" : 0
  },
  "hits" : {
    "total" : 5,
    "max_score" : 1.0,
    "hits" : [ {
      "_index" : "guestposts",
      "_type" : "post",
      "_id" : "AVISLIkudi58A4YBR78E",
      "_score" : 1.0,
      "_source":{"id":null,"headline":"Test Post #1","text":"Hi there, this works!","postedDate":1452003854529,"author":"Johannes"}
    }, {
      "_index" : "guestposts",
      "_type" : "post",
      "_id" : "AVISLJN5di58A4YBR78F",
      "_score" : 1.0,
      "_source":{"id":null,"headline":"Test Post #2","text":"Hi there, this works!","postedDate":1452003857272,"author":"Johannes"}
    }, {
      "_index" : "guestposts",
      "_type" : "post",
      "_id" : "AVISLJZmdi58A4YBR78G",
      "_score" : 1.0,
      "_source":{"id":null,"headline":"Test Post #3","text":"Hi there, this works!","postedDate":1452003858022,"author":"Johannes"}
    }, {
      "_index" : "guestposts",
      "_type" : "post",
      "_id" : "AVISLJcYdi58A4YBR78H",
      "_score" : 1.0,
      "_source":{"id":null,"headline":"Test Post #4","text":"Hi there, this works!","postedDate":1452003858200,"author":"Johannes"}
    }, {
      "_index" : "guestposts",
      "_type" : "post",
      "_id" : "AVISLVYDdi58A4YBR78I",
      "_score" : 1.0,
      "_source":{"id":null,"headline":"Test Post #5","text":"Hi there, this works!","postedDate":1452003907075,"author":"Johannes"}
    } ]
  }
}
```

perfect :-)

So, let´s start with the actual knockout part :-)


# Step 5 - Do some example knockout bindings with mappings plugin
Our app should look like an 90s guestbook. At the top of the page there should be the list of existing entries and at the bottom of the page there should be the form to add a new post.

Our template needs the following objects to initialize:

```
@(posts: ListModel, form: Form[NewEntry])(implicit request: play.api.mvc.Request[Any], messages: Messages)
```

We bind the given guestbook posts to an ui with the following code:

```
# html
  <div id="listmodel" data-bind="foreach: posts" data-model="@Json.toJson(posts)">
    <div class="panel panel-default">
      <div class="panel-heading" data-bind="text: headline"></div>
      <div class="panel-body" data-bind="text: text"></div>
      <div class="panel-footer">
        @Messages("posted.by") <span data-bind="text: author"></span> @Messages("posted.at") <span data-bind="text: postedDate"></span>
      </div>
    </div>
  </div>

# javascript
  var listmodel = $("#listmodel");
  var listViewModel = ko.mapping.fromJS(listmodel.data("model"));
  ko.applyBindings(listViewModel);
```

So we connect the playframework and knockout through json serialization and html data attributes. In step 2 we added the dependency to the ```knockout-mapping``` plugin. Through this plugin, the ```ko``` object is enhanced with the ```mapping``` functionality, shown in the javascript code.

The other markup attributes are knockout standards. ```data-bind``` indicates, that knockout should do "something" with the given expression. ```foreach``` indicates an iteration (who would have thought it?) and ```text``` displayes a text in the current dom element.

The formular to add a new post is not yet connected to knockout and is more or less plain playframework style, see:

```
<div id="formmodel" data-model="@Json.toJson(form.get)">
    <div class="well">
      @helper.form(routes.ApplicationController.create(), 'class -> "form-horizontal") {
        <fieldset>
          @if(form.hasErrors) {
            <p class="alert alert-danger">
            @form.errors.map { msg => @if(msg.key != "") {@Messages(msg.key): }@Messages(msg.message, msg.args: _*)<br/> }
            </p>
          }
          <legend>@Messages("post.new")</legend>
          <div class="form-group">
            <label class="col-lg-2 control-label" for="headline">@Messages("headline")</label>
            <div class="col-lg-10">
              <input class="form-control" id="headline" name="headline" type="text" placeholder="@Messages("headline")">
            </div>
          </div>

          <div class="form-group">
            <label class="col-lg-2 control-label" for="text">@Messages("text")</label>
            <div class="col-lg-10">
              <textarea class="form-control" id="text" name="text" rows="10"></textarea>
            </div>
          </div>

          <div class="form-group">
            <label class="col-lg-2 control-label" for="author">@Messages("author")</label>
            <div class="col-lg-10">
              <input class="form-control" id="author" name="author" type="text" placeholder="@Messages("author")">
            </div>
          </div>

          <button type="submit" class="btn btn-primary pull-right">@Messages("save")</button>
        </fieldset>
      }
    </div>
  </div>
```

Last point i want to mention is, that currently the play.api.data.Form is filled with optionals and no validation:

```
  val entryForm: Form[NewEntry] = Form(
    mapping(
      "headline" -> optional(text),
      "text" -> optional(text),
      "author" -> optional(text)
    )(NewEntry.apply)(NewEntry.unapply)
  )
```

This leads to the following (not so pretty) "getOrElse(..)" code during guestbook post creation:

```
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
```

We will discuss this issue in the next step. 


# Step 6 - Add validation plugin and have even more fun
