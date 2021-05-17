import smtplib
from email.message import EmailMessage

def email_alert(subject, body, to):
    msg = EmailMessage()
    msg.set_content(body)
    msg['subject'] = subject
    msg['to'] = to

    user = "princeflip59@gmail.com"
    password = "zntiywhjooduzndl"

    msg['from'] = user

    server = smtplib.SMTP("smtp.gmail.com", 587)
    server.starttls()
    server.login(user,password)
    server.send_message(msg)
    server.quit()


if __name__ == "__main__":
    email_alert("", "Hope you're having a great day! Your secret admirer", "2039435842@vtext.com")
