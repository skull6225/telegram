package org.telegram.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.services.utils.HelpBotSender;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerShippingQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingOption;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramService {

  private final HelpBotSender botSender;

  public TelegramService(HelpBotSender botSender) {
    this.botSender = botSender;
  }

  public void sendMessage(Long chatId, String text) {
    sendMessage(chatId, text, null);
  }

  public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
    SendMessage sendMessage = SendMessage
        .builder()
        .text(text)
        .chatId(chatId.toString())
        .parseMode(ParseMode.HTML)
        .replyMarkup(replyKeyboard)
        .build();

    execute(sendMessage);
  }

  public void editMessage(Long chatId, String text, Integer messageId) {
    EditMessageText editMessageText = EditMessageText
            .builder()
            .text(text)
            .chatId(chatId.toString())
            .messageId(messageId)
            .build();

    execute(editMessageText);
  }

  public void deleteMessage(Long chatId, Integer messageId) {
    DeleteMessage deleteMessage = DeleteMessage
            .builder()
            .chatId(chatId.toString())
            .messageId(messageId)
            .build();

    execute(deleteMessage);
  }

  public void sendInvoice(Long chatId, List<LabeledPrice> prices) {
    SendInvoice invoice = SendInvoice.builder()
        .chatId(chatId)
        .title("Замовлення")
        .description("Уважно перегляньте ваше замовлення")
        .payload("payload")
        .providerToken("632593626:TEST:sandbox_i32736567324")
        .currency("UAH")
        .prices(prices)
        .needShippingAddress(true)
        .startParameter("start_parameter")
        .needName(true)
        .needPhoneNumber(true)
        .needEmail(false)
        .isFlexible(true)
        .build();
    execute(invoice);
  }

  public void sendCheckoutAnswer(Long chatId, String id) {
    AnswerPreCheckoutQuery answerPreCheckoutQuery = AnswerPreCheckoutQuery.builder()
        .ok(true)
        .preCheckoutQueryId(id)
        .build();
    execute(answerPreCheckoutQuery);
  }

  public void sendShippingAnswer(String id, List<ShippingOption> shippingOptionList) {
    AnswerShippingQuery answerShippingQuery = AnswerShippingQuery.builder()
        .shippingQueryId(id)
        .ok(true)
        .shippingOptions(shippingOptionList)
        .build();
    execute(answerShippingQuery);
  }

  public void sendImage(Long chatId, String link, ReplyKeyboard replyKeyboard) throws TelegramApiException {
    SendPhoto sendPhoto = SendPhoto.builder()
        .chatId(chatId.toString())
        .photo(new InputFile(new File(link)))
        .parseMode(ParseMode.HTML)
        .replyMarkup(replyKeyboard)
        .build();

    botSender.execute(sendPhoto);
  }

  public void sendContact(Long chatId) {
    SendContact sendContact = SendContact.builder()
        .chatId(chatId.toString())
        .firstName("Company")
        .lastName("Name")
        .phoneNumber("+380981234567")
        .build();

    execute(sendContact);
  }

  private void execute(BotApiMethod botApiMethod) {
    try {
      botSender.execute(botApiMethod);
    } catch (Exception e) {
      log.error("Exception: ", e);
    }
  }
}
