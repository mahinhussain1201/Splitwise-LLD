package User;
import java.util.UUID;

public class User {
    private String id;
    private String name;
    private String email;

    public User(String name,String email){
        this.name=name;
        this.email=email;
        this.id=UUID.randomUUID().toString();
    }

    public String getId(){
        return id;
    }
    public String getEmail(){
        return email;
    }
    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return name+" ( "+email+" )";
    }
}
