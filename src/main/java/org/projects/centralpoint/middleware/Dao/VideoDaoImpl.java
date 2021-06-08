package org.projects.centralpoint.middleware.Dao;

import org.hibernate.proxy.HibernateProxy;
import org.projects.centralpoint.Database.DbSchema;
import org.projects.centralpoint.Database.VideoFilter;
import org.projects.centralpoint.Extern.Fetching.FetchingStatus;
import org.projects.centralpoint.Filter.SearchFilter;
import org.projects.centralpoint.middleware.Models.*;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository("videoDao")
public class VideoDaoImpl extends AbstractDao implements VideoDao
{
    public void SaveVideo(Video video)
    {
        if(video != null)
        {
            persist(video);
        }
    }

    public void DeleteVideoByName(String videoName)
    {
        //TODO do we have to add the schema in the query string before using it?
        Query query = getSession().createNativeQuery("delete from video where title = :title");
        query.setString("title", videoName);
        query.executeUpdate();
    }

    public List<Video> GetAllVideosByFilter(SearchFilter searchFilter, boolean fetchAdditionalInfo)
    {
        List<Video> result = null;
        VideoFilter qCreator = new VideoFilter(searchFilter, DbSchema.MAIN);

        if(qCreator.IsFilterSet())
        {
            result = qCreator.ApplyFilter(getSession());
        }

        if(fetchAdditionalInfo == true && result != null)
        {
            for(Video v : result)
            {
                if(v != null)
                {
                    Hibernate.initialize(v.getGenres());
                    Hibernate.initialize(v.getCountries());
                    Hibernate.initialize(v.getLanguages());
                    Hibernate.initialize(v.getActors());
                }
            }
        }

        return result;
    }

    public List<Video> GetWaitingVideos()
    {
        List<Video> result = null;

        // Create CriteriaBuilder
        CriteriaBuilder builder = getSession().getCriteriaBuilder();

        // Create CriteriaQuery
        CriteriaQuery<Video> criteria = builder.createQuery(Video.class);
        Root<Video> videoRoot = criteria.from(Video.class);

        criteria.where(builder.equal(videoRoot.get("externFetchingStatus"), FetchingStatus.WAITING.getIntValue()));
        result = getSession().createQuery(criteria).getResultList();

        if(result != null)
        {
            for(Video v : result)
            {
                if(v != null)
                {
                    Hibernate.initialize(v.getGenres());
                    Hibernate.initialize(v.getCountries());
                    Hibernate.initialize(v.getLanguages());
                    Hibernate.initialize(v.getActors());
                }
            }
        }

        return result;
    }

    public void updateVideo(Video video)
    {
        if(video != null)
        {
            update(video);
        }
    }

    public void updateVideos(List<Video> videos)
    {
        for(Video v : videos)
        {
            updateVideo(v);
        }
    }

    public Video GetVideoById(int id)
    {
        Criteria criteria = null;
        criteria = getSession().createCriteria(Video.class);
        criteria.add(Restrictions.eq("id", id));

        Video v = (Video)criteria.uniqueResult();

        if(v != null)
        {
            Hibernate.initialize(v.getGenres());
            Hibernate.initialize(v.getActors());
            Hibernate.initialize(v.getCountries());
            Hibernate.initialize(v.getLanguages());
        }

        return v;
    }
}
