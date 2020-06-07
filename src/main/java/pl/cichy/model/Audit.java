package pl.cichy.model;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
class Audit {

    //spring domyślnie zmienia camelcase na podkreślniki np createdOn na created_on dlatego w V4__ mamy nazwę created_on
    //aby postawić na swoim możemy dodać @Column(name: "createdOn") - jesteśmy silniejsi ;P
    //@Transient - kiedy nie chcemy mieć danego pola na bazie (np pesel, poza tym ze spring ma PESEL)
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    //operacja służąca do zapisu na bazie, do insertu
    @PrePersist
    void prePersist(){ createdOn = LocalDateTime.now(); }

    @PreUpdate
    void preMerge(){ updatedOn = LocalDateTime.now(); }


}

//super klase mozna zrobić na inny sposob, inaczej wykorzystac adnotacje
//@Inheritance(InheritanceType.TABLE_PER_CLASS)
//tu możliwe jest pójście na łatwizne wiec nie skorzystam
