package org.jboss.aerogear.geo.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

@Entity
@XmlRootElement
public class Application implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   @Version
   @Column(name = "version")
   private int version;

   @Column
   @Field(store = Store.YES)
   private String apiKey = UUID.randomUUID().toString();

   @Column
   private String apiSecret = UUID.randomUUID().toString();

   @Column
   private String name;
   
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

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (!(obj instanceof Application))
      {
         return false;
      }
      Application other = (Application) obj;
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

   public String getApiKey()
   {
      return apiKey;
   }

   public void setApiKey(String apiKey)
   {
      this.apiKey = apiKey;
   }

   public String getApiSecret()
   {
      return apiSecret;
   }

   public void setApiSecret(String apiSecret)
   {
      this.apiSecret = apiSecret;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }
   
   

@Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (id != null)
         result += "id: " + id;
      result += ", version: " + version;
      if (apiKey != null && !apiKey.trim().isEmpty())
         result += ", apiKey: " + apiKey;
      if (apiSecret != null && !apiSecret.trim().isEmpty())
         result += ", apiSecret: " + apiSecret;
      if (name != null && !name.trim().isEmpty())
         result += ", name: " + name;
      return result;
   }
}