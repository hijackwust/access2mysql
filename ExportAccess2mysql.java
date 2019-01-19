import java.sql.DriverManager;
import org.apache.commons.io.FileUtils;

public class ExportAccess2mysql {
    public static String mysql_quote ="`";

    public static void main(String[] args) {
        if(args.length !=4){
            System.out.println("Usage: java -cp .:./lib/*.jar ExportAccess [databaseFile] [tableArray] [username] [password] ");
            System.out.println("[databaseFile]   /Users/jessezhu/Architecture.mdb");
            System.out.println("[tableArray]     ATT_A3,ATT_A4,ATT_A5");
            System.out.println("[username]     \"\" ");
            System.out.println("[password]     \"\" ");
            System.exit(0);
        }
        String databaseFile =args[0];
        String tableArray =args[1];
        String username =args[2];
        String password =args[3];

        StringBuffer sqlScriptBuffer = new StringBuffer();

        java.sql.Connection conn =null;
        try {

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver").newInstance();
            String url = "jdbc:ucanaccess://"+databaseFile+";memory=true";

            String fieldSplit=",";

            conn = DriverManager.getConnection(url, username, password);

            java.sql.Statement st = conn.createStatement();

            String []  tableNameArray =tableArray.split(",");
            for(String tableName: tableNameArray) {
                java.sql.ResultSet rs = st.executeQuery("select * from " + tableName);

                sqlScriptBuffer.append("\n TRUNCATE TABLE " + mysql_quote + tableName + mysql_quote + "; ");

                java.sql.ResultSetMetaData md = rs.getMetaData();
                while (rs.next()) {
                    sqlScriptBuffer.append("\n INSERT INTO " + mysql_quote + tableName + mysql_quote + " ( ");


                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        if (i == md.getColumnCount()) {
                            fieldSplit = "";
                        } else {
                            fieldSplit = ",";
                        }
                        sqlScriptBuffer.append(mysql_quote + md.getColumnName(i) + mysql_quote + fieldSplit);
                    }
                    sqlScriptBuffer.append(" ) VALUES ( ");

                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        String columnTypeName = md.getColumnTypeName(i);

                        if (i == md.getColumnCount()) {
                            fieldSplit = "";
                        } else {
                            fieldSplit = ",";
                        }
                        if (columnTypeName.toUpperCase().contains("CHAR")) {
                            sqlScriptBuffer.append("'" + rs.getString(i) + "'" + fieldSplit);
                        } else if (columnTypeName.toUpperCase().contains("BINARY") || columnTypeName.toUpperCase().contains("BLOB")) {
                            sqlScriptBuffer.append("0x" + rs.getString(i) + fieldSplit);
                        } else {
                            sqlScriptBuffer.append(rs.getString(i) + fieldSplit);
                        }

                    }
                    sqlScriptBuffer.append(" ); \n");
                }

                rs.close();
            }

            FileUtils.writeStringToFile(new java.io.File("script.sql"), sqlScriptBuffer.toString(), "utf-8");

        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(conn!=null){
                try{
                    conn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
