package ATechnics.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Airbus")
public class AirbusTask {
    @Id
    @Column(name = "task_card")
    private String taskCard;
    @Column(name = "mpd")
    private String mpd;
    @Column(name = "multipliedmpd")
    private String multipliedMpd;
    @Column(name = "preparation")
    private String preparation;
    @Column(name = "multipliedpreparation")
    private String multipliedPreparation;
}