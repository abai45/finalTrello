package abai.kz.jpa.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_tasks")
public class Tasks extends BaseModel{
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    public enum TaskStatus {
        toDo(0), inTest(1), done(2), failed(3);
        private int value;
        TaskStatus(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    private TaskStatus status; // 0 - todo, 1 - intest, 2 - done, 3 - failed

    @ManyToOne
    Folders folder;
}
