package burunzhuy.tool.notificators;

import burunzhuy.interfaces.notification.PasswordRestoreSender;
import burunzhuy.entity.user.User;

abstract public class BasePasswordRestoreSender implements PasswordRestoreSender {

    protected User user;
    protected String direction;
    protected String token;

    public void setUser(User user)
    {
        this.user = user;
    }

    protected String getSubject()
    {
        return "Сброс пароля";
    }

    protected String getHtmlMessage()
    {
        return String.format("<b>Для сброса пароля вам потребуется ввести следующий код</b>\n" +
                "                <br/><h3>%s</h3><br/><br/>\n" +
                "                <i>Код действует в течении 30минут</i>", this.token);
    }

    public void setToken(String token){
        this.token = token;
    }
}
