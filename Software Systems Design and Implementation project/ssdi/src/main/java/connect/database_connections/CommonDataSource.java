package connect.database_connections;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;



final public class CommonDataSource extends AbstractDataSource {

	private static CommonDataSource datasource;

	private CommonDataSource() {
		super("SSDI_Project");
	}
	

	public static CommonDataSource getInstance() throws IOException, SQLException, PropertyVetoException {
		if (datasource == null) {
			synchronized (CommonDataSource.class) {
				if (datasource == null) {
					datasource = new CommonDataSource();
				}
			}
		}
		return datasource;
	}
	
}
