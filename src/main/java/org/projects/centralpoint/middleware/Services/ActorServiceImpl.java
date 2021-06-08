package org.projects.centralpoint.middleware.Services;

import org.projects.centralpoint.middleware.Dao.ActorDao;
import org.projects.centralpoint.middleware.Models.ActorJobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("actorService")
@Transactional
public class ActorServiceImpl implements ActorService
{
    @Autowired
    private ActorDao actorDao;

    public boolean actorExists(ActorJobs a)
    {
        return actorDao.actorExists(a);
    }
}
