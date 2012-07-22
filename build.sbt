name			:= "scbson"

organization	:= "de.djini"

version			:= "0.4.0"

scalaVersion	:= "2.9.2"

libraryDependencies	++= Seq(
	"de.djini"		%%	"scutil"	% "0.8.0"	% "compile",
	"de.djini"		%%	"scmirror"	% "0.4.0"	% "compile"
)

scalacOptions	++= Seq("-deprecation", "-unchecked")

(sourceGenerators in Compile)	<+= (sourceManaged in Compile) map Boilerplate.generate
