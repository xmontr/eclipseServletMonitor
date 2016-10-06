package remotejmxconnection.validator;

import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class NonEmptyStringFieldValidator implements IValidator {
	
	private String field;
	
	
	

	  public NonEmptyStringFieldValidator(String val) {
		super();
		this.field = val;
	}




	@Override
	  public IStatus validate(Object value) {
	    if (value instanceof String) {
	      String s = (String) value;
	      // We check if the string is longer then 2 signs
	      if (!s.isEmpty()) {
	        return ValidationStatus.ok();
	      } else {
	        return ValidationStatus
	            .error(field +"  cannot be empty");
	      }
	    } else {
	      throw new RuntimeException("Not supposed to be called for non-strings.");
	    }
	  }
	} 
