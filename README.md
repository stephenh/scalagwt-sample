Project that shows Scala and GWT work together to compile a few sample applications.

In order to run it you just need to type `ant` assuming you have [Apache Ant](http://ant.apache.org/)
installed. After compilation is done open of the html files in `war/` directory in a browser.

**NOTE**: Due to security restrictions samples will won't work in Google Chrome if
you load them from local file system. If you want to use Chrome, you must load files from
`war/` directory using http server.

This project uses custom versions of GWT and Scala. These are included in `lib/` directory for
released versions of a project. For snapshots, consult
[scalagwt@googlegroups.com](http://groups.google.com/group/scalagwt).

Have fun!
