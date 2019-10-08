package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Using OneToMany we have to have type of Collection that stores referenced values as Collection and nothing else
 * */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
    String mappedBy() default "";

    Class<?> typeOfReferencedObject();
}
