package com.github.funthomas424242.flowdesign;


import java.lang.annotation.*;

/**
 * Definition einer Operation nach javamagazin 7/2017 S.95 IOSP bzw. IODA:
 *
 * Operation=Logik auf Domänobjekten:
 * <ul>
 *     <li>Transformationen</li>
 *     <li>Domänenausdrücke</li>
 *     <li>Kontrollstrukturen mit Domänenausdrücken</li>
 *     <li>Aufrufe von APIs in 3th party libraries, nicht zum eigenen Kode</li>
 * </ul>

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
public @interface Operation {
}
