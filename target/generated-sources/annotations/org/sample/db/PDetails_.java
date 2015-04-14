package org.sample.db;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.sample.db.PProtein;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2015-04-14T22:16:13")
@StaticMetamodel(PDetails.class)
public class PDetails_ { 

    public static volatile SingularAttribute<PDetails, String> sunid;
    public static volatile SingularAttribute<PDetails, String> species;
    public static volatile SingularAttribute<PDetails, String> class1;
    public static volatile SingularAttribute<PDetails, String> superfamily;
    public static volatile SingularAttribute<PDetails, String> family;
    public static volatile SingularAttribute<PDetails, String> protein;
    public static volatile SingularAttribute<PDetails, PProtein> pid;
    public static volatile SingularAttribute<PDetails, String> fold;
    public static volatile SingularAttribute<PDetails, Integer> detid;

}