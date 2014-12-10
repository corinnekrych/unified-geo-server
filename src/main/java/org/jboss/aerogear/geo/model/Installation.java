package org.jboss.aerogear.geo.model;

import javax.persistence.Entity;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import java.lang.Override;

import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.SpatialMode;
import org.hibernate.search.spatial.Coordinates;
import org.jboss.aerogear.geo.model.Application;
import org.jboss.aerogear.geo.util.ApplicationFilterFactory;

import javax.xml.bind.annotation.XmlRootElement;

@Indexed
@Entity
@XmlRootElement
@FullTextFilterDefs( {
@FullTextFilterDef(name = "applicationFilter", impl = ApplicationFilterFactory.class)
})
public class Installation implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   @Version
   @Column(name = "version")
   private int version;

   @Column
   private String alias;

   @ManyToOne
   @IndexedEmbedded
   private Application application;

   @Column
   private double longitude;

   @Column
   private double latitude;

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Spatial(spatialMode = SpatialMode.HASH)
   public Coordinates getLocation()
   {
      return new Coordinates()
      {
         @Override
         public Double getLatitude()
         {
            return latitude;
         }

         @Override
         public Double getLongitude()
         {
            return longitude;
         }
      };
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof Installation))
      {
         return false;
      }
      Installation other = (Installation) obj;
      if (id != null)
      {
         if (!id.equals(other.id))
         {
            return false;
         }
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   public String getAlias()
   {
      return alias;
   }

   public void setAlias(String alias)
   {
      this.alias = alias;
   }

   public Application getApplication()
   {
      return application;
   }

   public void setApplication(Application application)
   {
      this.application = application;
   }

   public double getLongitude()
   {
      return longitude;
   }

   public void setLongitude(double longitude)
   {
      this.longitude = longitude;
   }

   public double getLatitude()
   {
      return latitude;
   }

   public void setLatitude(double latitude)
   {
      this.latitude = latitude;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (id != null)
         result += "id: " + id;
      result += ", version: " + version;
      if (alias != null && !alias.trim().isEmpty())
         result += ", alias: " + alias;
      if (application != null)
         result += ", application: " + application;
      result += ", longitude: " + longitude;
      result += ", latitude: " + latitude;
      return result;
   }
}