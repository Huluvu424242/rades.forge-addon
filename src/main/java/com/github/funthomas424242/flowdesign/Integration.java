package com.github.funthomas424242.flowdesign;


import java.lang.annotation.*;


/**
 * Definition von Integrationen nach javamagazin 7/2017 S.95 IOSP bzw. IODA:
 *
 * Integrationen = API Aufrufe in der eigenen Kodebasis
 * <ul>
 *     <li>API Aufrufe der eigenen Kodebasis</li>
 *     <li>keine Logik</li>
 *     <li>keine Domänenausdrücke</li>
 * </ul>
 *
 * IODA Architektur entspricht einem Baum:
 * <ul>
 *     <li>Wurzel: Programmeinstiegspunkt</li>
 *     <li>Integrationen als Astknoten</li>
 *     <li>Operationen als Blätter</li>
 * </ul>
 *
 * Integrationen können aus Operationen und Integrationen bestehen. Operationen nur aus Operationen.
 *
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Integration {
}
