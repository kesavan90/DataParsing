# DataParsing

Assumption --
     
     All the input data comes as JSON Array

Implemented Functions

    Out of order Handling

    Date Time format handling

    Base Data of LTV for all customers calculated and stored in Map- For further Analysis

Dependency

    Used Org.json for JSON parsing
  
Architecture
  
    Two Hashmap are used to store the LTV of Customer per week and Customer Information
  
    LTV of Customer per week has site visit count per week, amount ordered per week ,total amount and total site counts per year,LTV
  
     Customer Information can be correlated with Customer key and used for visualization purpose or to do exploratory analytics
  
Performance Improvement

    To handle large volume of data , Apache spark can be used to maintain the Life time value of customer
  
    Data can be saved in in-memory table or Hive table , query the data for Visualization
  
 
 P.S : LTV output of top N customer some has same values because of cooked up data
  
