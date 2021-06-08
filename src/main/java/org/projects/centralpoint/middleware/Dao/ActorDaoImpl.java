package org.projects.centralpoint.middleware.Dao;

import org.projects.centralpoint.middleware.Models.ActorJobs;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository("actorDao")
public class ActorDaoImpl extends AbstractDao implements ActorDao
{
    public boolean actorExists(ActorJobs actorJobs)
    {
        boolean state = false;
        if(actorJobs != null &&
           actorJobs.getPerson() != null &&
           actorJobs.getVideo() != null)
        {
            //TODO should the right schema be added in the string query before using it?
            String q = "SELECT * FROM actor_jobs WHERE people_key = :pKey AND video_key = :vKey";
            TypedQuery<ActorJobs> query = getSession().createNativeQuery(q, ActorJobs.class);
            query.setParameter("pKey", actorJobs.getPerson().getId());
            query.setParameter("vKey", actorJobs.getVideo().getId());

            try
            {
                if(query.getSingleResult() != null)
                    state = true;
            }
            catch(NoResultException nre)
            {
                // Ignore this
            }
        }
        return state;
    }

    public void SaveActor(ActorJobs actorJobs)
    {
        persist(actorJobs);
    }
}
