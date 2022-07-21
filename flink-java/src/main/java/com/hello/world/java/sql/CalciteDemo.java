package com.hello.world.java.sql;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

public class CalciteDemo {
    public static void main(String[] args) throws SqlParseException {
        SqlParser sqlParser = SqlParser.create("select * from source");
        SqlNode sqlNode = sqlParser.parseStmt();
        System.out.println(sqlNode);
    }
}
