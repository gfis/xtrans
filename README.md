xtrans
======

xtrans generates and consumes XML, SAX events and various Internet, office and legacy file formats (among them Base64, Quoted Printable, Hexdump, IBM z/OS JCL, DTA, Datev, SWIFT, Separated/Delimited, Morse Code, LDIF, Navy DIF, ICalendar, popular programming languages). By design all classes aim to be totally symmetrical, that is they should produce the same result when a format is converted to XML and then back to the format. In most cases this is accomplished to the byte level, and in the remaining cases the result of a generate - XML - serialize cycle differs from the original in the amount of whitespace only. The transfomers can be used as start and end points in XML processing pipelines. 
