package ee.ut.math.tvt.salessystem.dataobjects;
import javax.persistence.*;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "PURCHASE")
public class Purchase {
    @Id
    private Long id;

    @Column(name = "date")
    private String date;

    @Column(name = "time")
    private String Time;

    @Column(name = "total")
    private double sum;

    @OneToMany(mappedBy = "id")
    private Set<SoldItem> soldItems;
    
    public Purchase(Long id, double sum, Set<SoldItem> soldItems) {
        LocalDateTime timestamp = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        this.id = id;
        this.date = timestamp.getDayOfMonth() + "." + timestamp.getMonth() + "." + timestamp.getDayOfYear();
        this.Time = timestamp.getHour() + ":" + timestamp.getMinute() + ":" + timestamp.getSecond();
        this.sum = sum;
        this.soldItems = soldItems;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return Time;
    }

    public double getSum() {
        return sum;
    }

    public Set<SoldItem> getSoldItems() {
        return soldItems;
    }
}