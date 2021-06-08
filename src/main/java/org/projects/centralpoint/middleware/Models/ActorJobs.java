package org.projects.centralpoint.middleware.Models;

import javax.persistence.*;

@Entity
@Table(name = "actor_jobs")
public class ActorJobs
{
    @Id
    @Column(name = "actor_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "people_key")
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="video_key")
    private Video video;

    public Person getPerson() { return person; }
    public void setPerson(Person p)
    {
        this.person = p;
    }

    public Video getVideo() { return video; }
    public void setVideo(Video video)
    {
        this.video = video;
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean status = false;

        if (obj != null && obj instanceof ActorJobs)
        {
            ActorJobs actorJobs = ((ActorJobs)obj);
            if(actorJobs.getPerson() != null && this.getPerson() != null &&
               actorJobs.getPerson().getName().equals(this.getPerson().getName()) &&
               actorJobs.getVideo() != null && this.getVideo() != null &&
               actorJobs.getVideo().getId() == this.getVideo().getId())
            {
                status = true;
            }
        }

        return status;
    }
}
