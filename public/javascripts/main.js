$(function() {
  // bind list
  var listmodel = $("#listmodel");
  var listViewModel = ko.mapping.fromJS(listmodel.data("model"));
  ko.applyBindings(listViewModel);
});