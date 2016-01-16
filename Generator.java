import java.io.*;
import java.util.NoSuchElementException;
//import java.lang.reflect.Array;
import java.util.Scanner;

public class Generator {
	private File in;
	private File out;
	private Scanner scanner;
	private PrintWriter printer;
	private String currentString;
	private String nextString;
	private StringBuilder output = new StringBuilder("");
	
	public Generator(File in, File out) throws FileNotFoundException {
		this.in = in;
		this.out = out;
		this.scanner = new Scanner(this.in);
		this.printer = new PrintWriter(this.out);
	}
	public void create() throws InvalidSyntaxException {
		while(this.scanner.hasNext()) {
			this.currentString = this.scanner.nextLine();
			// catching empty strings and passing them 
			if (this.currentString.isEmpty()) {
				while(this.currentString.isEmpty()) {
					this.currentString = this.scanner.nextLine();
				}
			}
			
			if (!this.currentString.startsWith(" ")) { // cycle searching for the start of the table as it's name'
				String name = this.parseTableName(this.currentString); // let's get the name of the table
				String fields = this.createStatement(name); // parsing it's fields
				this.output.append(fields); // appending 
			}
		}
		
		this.printer.println(this.output.toString());
		
		this.scanner.close();
	}
	
	private String createStatement(String name) throws InvalidSyntaxException {
		StringBuilder fields = new StringBuilder();
		String temp = this.scanner.nextLine();  // "   Fields:"

		// VVVVV NOTE: you have to modify this part ot the code VVVVVV
		if (temp.contains("fields:") || temp.contains("Fields:") && this.calculateSpaces(temp) > 1) { // fields: initialization
			fields.append(name + "_id INT AUTO_INCREMENT PRIMARY KEY"); // adding as default PKey
			if (this.scanner.hasNext()) {
				this.currentString = this.scanner.nextLine(); // 1st field
			}
			if (this.calculateSpaces(this.currentString) > this.calculateSpaces(temp)) {
				int fieldSpaces = this.calculateSpaces(this.currentString); // spaces before a field of the table
				
				while (fieldSpaces == this.calculateSpaces(this.currentString) && this.scanner.hasNext()) { // adding field by field		
					fields.append(this.parseTableField(name));
					this.currentString = this.scanner.nextLine();
				}
				if (this.calculateSpaces(this.currentString) == fieldSpaces) { // if the last row is also a tableField 
					fields.append(this.parseTableField(name));
				}				
			} else {                                //   fields:
				throw new InvalidSyntaxException(); //  someField: varchar(50) or if no Fields: mentioned
			}
		}
		// ^^^^^^^ NOTE: end of modification part ^^^^^^^
		fields.append("\n);\n");
		
		return fields.toString();
    }
	
	
	private String parseTableName(String string) throws InvalidSyntaxException {
		if(string.charAt(string.length() - 1) != ':') { // if the string doesn't end with ':'
			throw new InvalidSyntaxException();
		}
		this.output.append("Create table " + string.substring(0, string.length() - 1) + "(\n");
		return string.substring(0, string.length() - 1); 
	}
	
	private String parseTableField(String tableName) {
	    return ",\n" + tableName + "_" + this.currentString.split(":")[0].replaceAll("\\s","") + this.currentString.split(":")[1];
	}
	
	
	private int calculateSpaces(String string) {
		int i = 0;
		if (string.isEmpty()) {
			return -1;
		}
		for ( ; string.charAt(i) == ' '; i++);
		return i;
	}
	
	
	
	//main
	public static void main (String[] args) throws FileNotFoundException, InvalidSyntaxException {
		File in = new File("C:/MinGW/msys/1.0/home/user/Generator/file.txt");
		File out = new File("C:/MinGW/msys/1.0/home/user/Generator/out.txt");
		
		Generator a = new Generator (in, out);
		a.create();
		
		
	}


}
	
