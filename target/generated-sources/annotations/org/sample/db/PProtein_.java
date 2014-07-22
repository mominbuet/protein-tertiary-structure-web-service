package org.sample.db;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.sample.db.Comogphogfeature;
import org.sample.db.PCamat32;
import org.sample.db.PDetails;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-07-23T02:48:19")
@StaticMetamodel(PProtein.class)
public class PProtein_ { 

    public static volatile SingularAttribute<PProtein, Integer> dataset;
    public static volatile SingularAttribute<PProtein, String> pdbid;
    public static volatile SingularAttribute<PProtein, String> histogram128;
    public static volatile SingularAttribute<PProtein, PDetails> pDetails;
    public static volatile SingularAttribute<PProtein, String> name;
    public static volatile CollectionAttribute<PProtein, Comogphogfeature> comogphogfeatureCollection;
    public static volatile SingularAttribute<PProtein, String> histogram;
    public static volatile SingularAttribute<PProtein, Integer> pid;
    public static volatile SingularAttribute<PProtein, String> scopsid;
    public static volatile CollectionAttribute<PProtein, PCamat32> pCamat32Collection;

}