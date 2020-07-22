package demoapp.dom.annotDomain.Property.mustSatisfy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;

import lombok.RequiredArgsConstructor;

@Action(
    semantics = SemanticsOf.IDEMPOTENT,
    associateWith = "customerAgePropertyUsingAnnotation", associateWithSequence = "1"
    , hidden = Where.EVERYWHERE  // TODO: ISIS-2415
)
@RequiredArgsConstructor
public class PropertyMustSatisfyVm_updateWithParameterLayout {

    private final PropertyMustSatisfyVm propertyMustSatisfyVm;

//tag::annotation[]
    public PropertyMustSatisfyVm act(
            @Parameter(
                mustSatisfy = OfWorkingAgeSpecification.class   // <.>
                , optionality = Optionality.OPTIONAL
            )
            @ParameterLayout(
                describedAs =
                    "@Parameter(mustSatisfy = OfWorkingAgeSpecification.class)"
            )
            final Integer customerAgeParameterUsingAnnotation) {
        propertyMustSatisfyVm.setCustomerAgePropertyUsingAnnotation(customerAgeParameterUsingAnnotation);
        return propertyMustSatisfyVm;
    }
//end::annotation[]
    public Integer default0Act() {
        return propertyMustSatisfyVm.getCustomerAgePropertyUsingAnnotation();
    }

}
