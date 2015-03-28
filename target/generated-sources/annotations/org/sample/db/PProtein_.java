package org.sample.db;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.sample.db.Comogphogfeature;
import org.sample.db.PDetails;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2015-03-28T09:52:44")
@StaticMetamodel(PProtein.class)
public class PProtein_ { 

    public static volatile SingularAttribute<PProtein, Integer> dataset;
    public static volatile SingularAttribute<PProtein, String> pdbid;
    public static volatile SingularAttribute<PProtein, PDetails> pDetails;
    public static volatile SingularAttribute<PProtein, String> name;
    public static volatile CollectionAttribute<PProtein, Comogphogfeature> comogphogfeatureCollection;
    public static volatile SingularAttribute<PProtein, Integer> pid;
    public static volatile SingularAttribute<PProtein, String> scopsid;

}