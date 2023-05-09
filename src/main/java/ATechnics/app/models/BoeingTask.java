package ATechnics.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "boeing")
public class BoeingTask {
    @Id
    @Column(name = "task_card")
    private String taskCard;
    @Column(name = "mpd")
    private String mpd;
    @Column(name = "multiplied_mpd")
    private String multipliedMpd;
}