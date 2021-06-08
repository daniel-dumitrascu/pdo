package org.projects.centralpoint.middleware.Dao;

import org.projects.centralpoint.middleware.Models.ActorJobs;


public interface ActorDao
{
    boolean actorExists(ActorJobs actorJobs);

    void SaveActor(ActorJobs actorJobs);
}
