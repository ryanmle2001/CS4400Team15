import java.util.*;
import java.io.*;
public class InsertGenerator {

	public static void main(String[] args) {
		transactionAndContains();
	}

	public static void catalogItem() {
		try {
			String tableName = "catalog_item";
			Scanner scan = new Scanner(new File("CatalogItem.csv"));
			PrintWriter pw = new PrintWriter("catalog_item_inserts.txt");
			String insertStatement = "INSERT INTO " + tableName + " VALUES ";
			scan.nextLine();
			while(scan.hasNextLine()) {
				insertStatement += "(";
				String[] entities = scan.nextLine().split(",");
				if (entities.length == 4) {
					String[] newEntities = {"Buy Personal Protective Equipment, Inc", entities[2], entities[3]};
					entities = newEntities;
				}
				if (scan.hasNextLine()) {
					insertStatement += String.format("'%s','%s',%s),", entities[1], entities[0], entities[2].trim().substring(1));
				} else {
					insertStatement += String.format("'%s','%s',%s)", entities[1], entities[0], entities[2].trim().substring(1));
				}
			}
			insertStatement += ";";
			pw.print(insertStatement);
			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} finally {
			// scan.close();
		}
	}

	public static void transactionAndContains() {
		try {
			String tableName1 = "transactions";
			Scanner scan = new Scanner(new File("Transaction.csv"));
			PrintWriter pw1 = new PrintWriter("transaction_insert.txt");
			String insertStatement1 = "INSERT INTO " + tableName1 + " VALUES ";
			String tableName2 = "contains_item";
			PrintWriter pw2 = new PrintWriter("contains_item_insert.txt");
			String insertStatement2 = "INSERT INTO " + tableName2 + " VALUES ";
			scan.nextLine();
			ArrayList<String> pidList = new ArrayList<String>();
			while(scan.hasNextLine()) {		
				String[] entities = scan.nextLine().split(",");
				if (!(pidList.contains(entities[0]))) {
					insertStatement1 += "(";
					String formattedDate = dateFormat(entities[1]);
					String[] hospitalName = entities[2].split("'");
					if (hospitalName.length == 2) {
						entities[2] = hospitalName[0] + "\\'" + hospitalName[1]; 
					}
					// t-id, hospital, date
					insertStatement1 += String.format("%s,'%s','%s'),", entities[0], entities[2], formattedDate);
					pidList.add(entities[0]);
				}
				insertStatement2 += "(";
				if (entities.length == 7) {
					String[] newEntities = {entities[0], entities[1], entities[2], "Buy Personal Protective Equipment, Inc", entities[5], entities[6]};
					entities = newEntities;
				}
				// p-id string, t-id decimal, item_count decimal, business-name string
				insertStatement2 += String.format("'%s',%s,%s,'%s'),", entities[4], entities[0], entities[5],entities[3]);
			}
			insertStatement1 = insertStatement1.substring(0, insertStatement1.length() - 1);
			insertStatement1 += ";";
			insertStatement2 = insertStatement2.substring(0, insertStatement2.length() - 1);
			insertStatement2 += ";";
			pw1.print(insertStatement1);
			pw1.close();
			pw2.print(insertStatement2);
			pw2.close();
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} 

	}

	public static String dateFormat(String date) {
		String[] dateParts = date.split("/");
		if (dateParts[0].length() == 1) {
			dateParts[0] = "0" + dateParts[0];
		}
		if (dateParts[1].length() == 1) {
			dateParts[1] = "0" + dateParts[1];
		}
		while (dateParts[2].length() < 4) {
			dateParts[2] = "0" + dateParts[2];
		}
		return dateParts[0] + "-" + dateParts[1] + "-" + dateParts[2];
	}
}