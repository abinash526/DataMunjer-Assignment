package com.stackroute.datamunger;
import java.util.*;

/*There are total 5 DataMungertest files:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class DataMunger {

	/*
	 * This method will split the query string based on space into an array of words
	 * and display it on console
	 */

	public String[] getSplitStrings(String queryString) {

		return queryString.toLowerCase().split(" ");
	}

	/*
	 * Extract the name of the file from the query. File name can be found after a
	 * space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc.
	 * 
	 * Please consider this while extracting the file name in this method.
	 */

	public String getFileName(String queryString) {
	    int pos1=queryString.lastIndexOf("from");
	    int pos2=queryString.lastIndexOf("csv");
	    String outStr=queryString.substring(pos1+5,pos2+3);

	   // String[] inputStr=queryString.split(" ");
		return outStr;
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */
	
	public String getBaseQuery(String queryString) {
	    if(queryString.contains("where")){
	    String[] str1=queryString.split("where");
	   // String[] str2;

	    str1[0]=str1[0].trim();
	    return str1[0];
	    }
	    String[] str2;
        if(queryString.contains("group by")||queryString.contains("order by"))
        {
            str2=queryString.split("group by|order by");
            str2[0]= str2[0].trim();
            return str2[0];
        }
	    else
	        return queryString;

		//return queryString.substring(0,pos1+3);
	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 * 
	 * Note: 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
	 * name can contain '*'
	 * 
	 */
	
	public String[] getFields(String queryString) {
	    int pos1=queryString.lastIndexOf("select");
        int pos2=queryString.lastIndexOf("from");
        String outP=queryString.substring(pos1+7,pos2-1);
        String[] arrOutP=outP.split(",");

		return arrOutP;
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note:  1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */
	
	public String getConditionsPartQuery(String queryString) {
	    if(queryString.contains("where")) {
            if (queryString.contains("group by")) {
                //int pos1=queryString.lastIndexOf("where");
                String[] str1 = queryString.split("where");
                str1[1] = str1[1].trim();
                String[] str2 = str1[1].split("group by");
                str2[0] = str2[0].trim();
                System.out.println(" group by and where");
                return str2[0].toLowerCase();
                //int pos2=queryString.lastIndexOf("group by");

            } else if (queryString.contains("order by")) {
                String[] str1 = queryString.split("where");
                System.out.println(" order by and where");
                str1[1] = str1[1].trim();
                String[] str2 = str1[1].split("order by");
                str2[0] = str2[0].trim();
                return str2[0].toLowerCase();

            } else {
                String[] str1 = queryString.split("where");
                str1[1] = str1[1].trim();
                System.out.println("Only where");
                return str1[1].toLowerCase();

            }

        }
	    else
        {
            return null;
        }

	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014","city ='bangalore'"]
	 * and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String[] getConditions(String queryString) {

        String[] str2=null,str3=null,res=null;
        String temp;
        if(queryString.contains("where"))
        {
            queryString = queryString.toLowerCase();
            String[] whereQuery;

            String tempString;
            String[] conditionQuery;
            String[] getCondition = null;
            if (queryString.contains("where")) {
                whereQuery = queryString.trim().split("where ");
                if (whereQuery[1].contains("group by")) {
                    conditionQuery = whereQuery[1].trim().split("group by");
                    tempString = conditionQuery[0];
                } else if (whereQuery[1].contains("order by")) {
                    conditionQuery = whereQuery[1].trim().split("order by");
                    tempString = conditionQuery[0];
                } else {
                    tempString = whereQuery[1];
                }
                getCondition = tempString.toLowerCase().trim().split(" and | or ");
                for (String s : getCondition) {

                }

            }
            return getCondition;
	}
return null;
	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note:  1. AND/OR
	 * keyword will exist in the query only if where conditions exists and it
	 * contains multiple conditions. 2. AND/OR can exist as a substring in the
	 * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
	 * these as well when extracting the logical operators.
	 * 
	 */}

	public String[] getLogicalOperators(String queryString) {
        queryString = queryString.toLowerCase();
        String[] str1 = queryString.split(" ");
        String obj = "";
        if (queryString.contains("where ")) {
            for (int i = 0; i < str1.length; i++) {
                if (str1[i].equals("and")||str1[i].equals("or")||str1[i].equals("not")) {

                    obj += str1[i] + " ";
                }
            }
            return obj.split(" ");
        }
        return null;


	}

	/*
	 * This method extracts the order by fields from the query string. Note: 
	 * 1. The query string can contain more than one order by fields. 2. The query
	 * string might not contain order by clause at all. 3. The field names,condition
	 * values might contain "order" as a substring. For eg:order_number,job_order
	 * Consider this while extracting the order by fields
	 */

	public String[] getOrderByFields(String queryString) {
	 if(queryString.contains("order by"))
     {
         String[] outP=queryString.split("order by ");
         return outP[1].split(" ");
     }
		return null;
	}

	/*
	 * This method extracts the group by fields from the query string. Note:
	 * 1. The query string can contain more than one group by fields. 2. The query
	 * string might not contain group by clause at all. 3. The field names,condition
	 * values might contain "group" as a substring. For eg: newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */

	public String[] getGroupByFields(String queryString) {
        if(queryString.contains("group by"))
        {
            String[] outP=queryString.split("group by ");
            return outP[1].split(" ");
        }
        return null;
    }




	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
	 * followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */

	public String[] getAggregateFunctions(String queryString) {
	    String[] arrOut=queryString.split(" |,");
	    String outp="";
	    for(int i=0;i<arrOut.length;i++)
        {
            if(arrOut[i].contains("("))
            {
                outp=outp+arrOut[i]+" ";
            }
        }
	    if(queryString.contains("("))
	        return outp.split(" ");

		return null;
	}

}