package code.challenge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Scanner;

public class ShutterFly {
    public static Map<String, Map<String, Map<String, Double>>> db = new HashMap<String, Map<String, Map<String, Double>>>();
    //Map to store the data of all customers for different year for different week in a year
    //LTV will calculated and stored in this Map.Total amount ordered,Total site visit for that particular week also store for further analysis
    public static Map<String, List<String>> cust_info = new HashMap<String, List<String>>();
    //Customer Information is maintained in a separate map for correlation with customer key
    public static Map<String, Double> LTV = new HashMap<String, Double>();
    //Mpa for All customer to hold the Life time value
    public static int i = 0;

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\VITRIAK\\Documents\\Personal\\Shutterfly\\data.txt")); // Input Json file
            // Assumption is all json data will JSON Array
            String Input_text;
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                Input_text = sb.toString();
            } finally {
                br.close();
            }

            JSONArray jsonarray = new JSONArray(Input_text);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Read(jsonobject);

            }

            SimpleLTV(db);

            Scanner scan = new Scanner(System.in);
            System.out.println("Enter the value for top x customers with the highest Simple Lifetime Value : ");
            int n = scan.nextInt();
            //20int n =20;
            TopXSimpleLTVCustomers(n, LTV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Read(JSONObject jsondata) { // Method to read the input data
        try {
            String verb = jsondata.getString("verb");
            String type = jsondata.getString("type");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss.SSS'Z'");
            String date1 = (jsondata.getString("event_time"));
            TsValidFormat valid = new TsValidFormat();
            Date date = valid.TsValidFormat(date1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String week = Integer.toString(cal.get(Calendar.WEEK_OF_YEAR));
            String year = Integer.toString(cal.get(Calendar.YEAR));
            if (type.equals("CUSTOMER") && verb.equals("NEW")) { // If the customer is new, it needs to get registered
                String key = jsondata.getString("key");
                Register(key, year);
                String name = jsondata.getString("last_name");
                String acity = jsondata.getString("adr_city");
                String astate = jsondata.getString("adr_state");
                cust_register(key, name, acity, astate);
            }
            if (type.equals("SITE_VISIT")) { // If the data is site visit , site visit will be incremented
                String customer_id = jsondata.getString("customer_id");
                Calculate(customer_id, year, week, "");
            }
            if (type.equals("ORDER")) { // If the data us Order , amount will incremented and LTV will be calcuated and stored in Map
                String customer_id = jsondata.getString("customer_id");
                String amount = (jsondata.getString("total_amount"));
                Calculate(customer_id, year, week, amount);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    public static void Register(String key, String year) {
        try {
            Map<String, Map<String, Map<String, Double>>> tempmap = new HashMap<String, Map<String, Map<String, Double>>>();
            final String year1 = year;
            if (db.get(key) == null) {
                db.put(key, new HashMap() {{
                    put(year1, new HashMap<>());
                }});
            }
            if (db.get(key).get(year) == null) {
                db.get(key).put(year, new HashMap<String, Double>());
            }

            for (i = 1; i <= 52; i++) {
                db.get(key).get(year).put("Week_" + i, 0d);
                db.get(key).get(year).put("Amount_" + i, 0d);
            }

            db.get(key).get(year).put("Total_week", 0d);
            db.get(key).get(year).put("Total_Amount", 0d);
            db.get(key).get(year).put("Total_SiteCount", 0d);
            //Insert(tempmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Insert(Map insertdata) {
        db.putAll(insertdata);
    }

    public static void Calculate(String cust_id, String year, String week_no, String amount) { // LTV Calculation
        try {
            if (db.containsKey(cust_id)) {
                if (db.get(cust_id).containsKey(year)) {
                    if (db.get(cust_id).get(year).containsKey("Week_" + week_no)) {
                        if (amount == "") {
                            Double w_count = db.get(cust_id).get(year).get("Week_" + week_no) + 1d;
                            Double s_count = db.get(cust_id).get(year).get("Total_SiteCount") + 1d;
                            db.get(cust_id).get(year).put("Total_SiteCount", s_count);
                            db.get(cust_id).get(year).put("Week_" + week_no, w_count);

                        }
                        if (amount != "") {
                            amount.substring(0, amount.length() - 4);
                            Double amount_1 = Double.parseDouble(amount.substring(0, amount.length() - 4));
                            Double a_count = db.get(cust_id).get(year).get("Amount_" + week_no) + amount_1;
                            db.get(cust_id).get(year).put("Amount_" + week_no, a_count);
                            Double Total_a_count = db.get(cust_id).get(year).get("Total_Amount") + amount_1;
                            db.get(cust_id).get(year).put("Total_Amount", Total_a_count);
                            db.get(cust_id).get(year).put("LTV_" + week_no, db.get(cust_id).get(year).get("Amount_" + week_no) / db.get(cust_id).get(year).get("Week_" + week_no));

                        }
                    }
                } else {
                    Register(cust_id, year);
                    Calculate(cust_id, year, week_no, amount);
                }
            } else {
                Register(cust_id, year);
                Calculate(cust_id, year, week_no, amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void cust_register(String key, String lname, String city, String state) { // Customer Information Map
        List<String> custlist = new ArrayList<String>();
        custlist.add(lname);
        custlist.add(city);
        custlist.add(state);
        cust_info.put(key, custlist);

    }

    public static void SimpleLTV(Map database) {
        Iterator<Map.Entry<String, Map<String, Map<String, Double>>>> key = database.entrySet().iterator();
        while (key.hasNext()) {
            Map.Entry<String, Map<String, Map<String, Double>>> parentPair = key.next();
            //System.out.println("CustomerKey :   " + parentPair.getKey() + "  :  " + parentPair.getValue());

            Iterator<Map.Entry<String, Map<String, Double>>> year = (parentPair.getValue()).entrySet().iterator();
            while (year.hasNext()) {
                Map.Entry<String, Map<String, Double>> childPair = year.next();
                //System.out.println("Year :   " + childPair.getKey() + "  :  " + childPair.getValue());

                Iterator<Map.Entry<String, Double>> child1 = (childPair.getValue()).entrySet().iterator();

                while (child1.hasNext()) {
                    Map.Entry childPair1 = child1.next();
                    if ((childPair1.getKey().toString().matches("^LTV_.*")) && Double.parseDouble(childPair1.getValue().toString()) > 0.0) {
                        //Optional to print out the LTV of all customers
                        //System.out.println("Customer Key " + parentPair.getKey() + "    " + "Year  " + childPair.getKey() + "    " + "Week  " + childPair1.getKey().toString().substring(4, childPair1.getKey().toString().length()) + "   " + "LTV  " + 52 * Double.parseDouble(childPair1.getValue().toString()) * 10);
                        LTV.put("CustomerKey_" + parentPair.getKey() + "_Year_" + childPair.getKey() + "_week_" + childPair1.getKey().toString().substring(4, childPair1.getKey().toString().length()), 52 * Double.parseDouble(childPair1.getValue().toString()) * 10);
                    }
                    //child.remove(); // avoids a ConcurrentModificationException
                }
            }

        }

    }

    public static void TopXSimpleLTVCustomers(int x, Map LTV) { // Top X Customers by LTV
        SorttByValue sbv = new SorttByValue();

        sbv.printMap(x, sbv.sortByValue(LTV));

    }
}
