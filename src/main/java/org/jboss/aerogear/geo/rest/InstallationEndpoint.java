package org.jboss.aerogear.geo.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.jboss.aerogear.geo.model.Application;
import org.jboss.aerogear.geo.model.Installation;
import org.jboss.aerogear.geo.util.HttpBasicHelper;

/**
 * 
 */
@Stateless
@Path("/installations")
public class InstallationEndpoint {
	@PersistenceContext(unitName = "unified-geo-server-persistence-unit")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(Installation entity,
			@Context HttpServletRequest request) {
		Application application = loadVariantWhenAuthorized(request);
		entity.setApplication(application);
		// new or update ?
		if (findByAlias(entity.getAlias()) != null) {
			em.merge(entity);
		} else {
			em.persist(entity);
		}

		return Response.created(
				UriBuilder.fromResource(InstallationEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Installation entity = em.find(Installation.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		TypedQuery<Installation> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM Installation i WHERE i.id = :entityId ORDER BY i.id",
						Installation.class);
		findByIdQuery.setParameter("entityId", id);
		Installation entity;
		try {
			entity = findByIdQuery.getSingleResult();
			System.out.println("Belongs to "
					+ entity.getApplication().getName());
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	private Installation findByAlias(String alias) {
		TypedQuery<Installation> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM Installation i WHERE i.alias = :alias ORDER BY i.id",
						Installation.class);
		findByIdQuery.setParameter("alias", alias);
		Installation entity;
		try {
			entity = findByIdQuery.getSingleResult();
			System.out.println("Belongs to "
					+ entity.getApplication().getName());
		} catch (NoResultException nre) {
			entity = null;
		}

		return entity;
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findByApplicationId(@PathParam("id") Long id) {
		TypedQuery<Installation> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM Installation i WHERE i.id = :entityId ORDER BY i.id",
						Installation.class);
		findByIdQuery.setParameter("entityId", id);
		Installation entity;
		try {
			entity = findByIdQuery.getSingleResult();
			System.out.println("Belongs to "
					+ entity.getApplication().getName());
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public List<Installation> listAll(
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult,
			@QueryParam("applicationId") Long applicationId) {
		TypedQuery<Installation> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM Installation i WHERE i.application.id = :applicationId ORDER BY i.id",
						Installation.class);
		findAllQuery.setParameter("applicationId", applicationId);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Installation> results = findAllQuery.getResultList();
		return results;
	}

	@GET
	@Path("/geosearch")
	@Produces("application/json")
	public List<Installation> geosearch(@Context HttpServletRequest request, @QueryParam("alias") String alias,
			@QueryParam("latitude") Double latitude,
			@QueryParam("longitude") Double longitude,
			@QueryParam("radius") Double radius) {
		Application application = loadVariantWhenAuthorized(request);
		if(application != null){
		if (alias != null) {
			Installation installation = findByAlias(alias);
			longitude = installation.getLongitude();
			latitude = installation.getLatitude();
		}

		FullTextEntityManager fullText = Search.getFullTextEntityManager(em);
		
		QueryBuilder builder = fullText.getSearchFactory().buildQueryBuilder()
				.forEntity(Installation.class).get();

		org.apache.lucene.search.Query luceneQuery = builder.spatial()
				.onField("location").within(radius, Unit.KM)
				.ofLatitude(latitude).andLongitude(longitude).createQuery();
       
		FullTextQuery query = fullText.createFullTextQuery(luceneQuery,
				Installation.class);
		//query.enableFullTextFilter("applicationFilter").setParameter("apiKey", application.getApiKey());
		List<Installation> installations = query.getResultList();
		return filterInstallations(installations, application.getApiKey()); //far from optimal but hibernate search drive me nuts
		}
		else {
			return null;
		}
		
	}

	private List<Installation> filterInstallations(List<Installation> installations,
			String apiKey) {
		List<Installation> filteredList = new ArrayList<Installation>();
		for(Installation installation : installations) {
			if(installation.getApplication().getApiKey().equals(apiKey)){
				filteredList.add(installation);
			}
		}
		return filteredList;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(Installation entity) {
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}

	/**
	 * returns application if the masterSecret is valid for the request
	 * PushApplicationEntity
	 */
	private Application loadVariantWhenAuthorized(HttpServletRequest request) {

		String[] credentials = HttpBasicHelper
				.extractUsernameAndPasswordFromBasicHeader(request);
		String applicationID = credentials[0];
		String secret = credentials[1];

		final Application application = findApplication(applicationID);
		if (application != null && application.getApiSecret().equals(secret)) {
			return application;
		}

		// unauthorized...
		return null;
	}

	private Application findApplication(String applicationId) {
		TypedQuery<Application> findByApiKeyQuery = em
				.createQuery(
						"SELECT DISTINCT a FROM Application a WHERE a.apiKey = :entityId ORDER BY a.id",
						Application.class);
		findByApiKeyQuery.setParameter("entityId", applicationId);
		Application entity;
		try {
			entity = findByApiKeyQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}

		return entity;
	}
}
