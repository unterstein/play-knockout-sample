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

# Step 3 - Init and use webjars and add some styling

# Step 4 - Add (sample) data model and repositories (used spring-data-elasticsearch through my play-elasticplugin)

# Step 5 - Do some example knockout bindings with mappings plugin

# Step 6 - Add validation plugin and have even more fun
