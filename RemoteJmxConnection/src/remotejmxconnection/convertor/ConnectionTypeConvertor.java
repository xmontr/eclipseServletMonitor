package remotejmxconnection.convertor;

import org.eclipse.core.databinding.conversion.Converter;

import remotejmxconnection.RemoteConnectiontype;

public class ConnectionTypeConvertor extends Converter {
	
	private Class from;
	private Class to;
	

	

	public ConnectionTypeConvertor(Object fromType, Object toType) {
		super(fromType, toType);
		from = (Class) fromType;
		to=(Class) toType;
	}

	@Override
	public Object convert(Object fromObject) {
		Object ret = null;
		if( fromObject instanceof RemoteConnectiontype ){
			RemoteConnectiontype	ct =(RemoteConnectiontype) fromObject;
			ret = ct.name();
			
		}
		if( fromObject instanceof String ){
			String	st =(String) fromObject;
			ret = RemoteConnectiontype.valueOf(st);
			
		}
		
		
		
		return ret;
	}

}
