package UI.RatingScreen;

import Manager.TransPoolManager;
import UI.StartScreen.MyController;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class RateWindowController {

    @FXML
    private JFXComboBox<Integer> RateComboBox;
    @FXML
    private TextField RatingComment;

    @FXML
    private Button AddReviewButton;

    @FXML
    private Label addedReview;

    private TransPoolManager manager = MyController.getTransPoolManager();
    private String text;
    private Integer rate = 0;

    @FXML
    private void initialize() {
        for (int i = 1; i <= 5; i++)
            RateComboBox.getItems().add(i);
        AddReviewButton.setDisable(true);

    }

    public void RateComboBoxClick(ActionEvent actionEvent) {
        if (RateComboBox.getSelectionModel().getSelectedItem() != null) {
            setRate(RateComboBox.getSelectionModel().getSelectedItem());
            AddReviewButton.setDisable(false);
        }
    }

    // public void RatingCommentAction(MouseEvent mouseEvent) {

    // setText( RatingComment.getText());
    //  }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public void OnAddRequestClicked(ActionEvent actionEvent) {
        boolean didRatingAccepted;
        String text1 = RatingComment.getText();
        if (text1 == null) {
            didRatingAccepted = manager.AddDriversRating(rate, null);
        } else {
            didRatingAccepted = manager.AddDriversRating(rate, text1);
            RatingComment.setDisable(true);
            RateComboBox.setDisable(true);
            AddReviewButton.setDisable(true);
            if (didRatingAccepted == false)
                addedReview.setText("This Driver's Rating was previously added");

            addedReview.setVisible(true);


        }
    }
}

