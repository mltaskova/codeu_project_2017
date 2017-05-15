// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.chat.client.simplegui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import codeu.chat.client.ClientContext;
import codeu.chat.common.ConversationSummary;
import codeu.chat.common.Message;
import codeu.chat.common.User;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

// NOTE: JPanel is serializable, but there is no need to serialize MessagePanel
// without the @SuppressWarnings, the compiler will complain of no override for serialVersionUID
@SuppressWarnings("serial")
public final class MessagePanel extends JPanel {

    private final JLabel messageOwnerLabel = new JLabel("Owner:", 4);
    private final JLabel messageConversationLabel = new JLabel("Conversation:", 2);
    private final DefaultListModel<String> messageListModel = new DefaultListModel();
    private final ClientContext clientContext;

    /**
     * TextAreas for getting input and displaying output
     **/
    private JTextArea plain;
    private JTextArea cipher;
    /**
     * Buttons for returning output
     **/
    private JButton encrypt;
    private JButton decrypt;
    /**
     * JComboBox that allows the user to choose encryption algorithms
     **/
    private JComboBox<String> encryptionType;
    /**
     * String of input and output
     **/
    private String plainText;
    private String cipherText;
    /**
     * Strings of shortened names of the encryption algorithms
     **/
    private String copy = "Copy";
    private String caesar = "Caesar cipher";
    private String rail = "Rail fence";
    private String mono = "Monoalphabetic cipher";
    private String vigen = "Vigenere cipher";
    private String greek = "Greek";
    /**
     * Logic behind the encryption and decryption
     **/
    //private EncryptionStrategy strategy;

    /**
     * Add the buttons and combo box to the GUI
     */
    private void addButtons(JPanel p) {
        // set up JComboBox
        String[] encryptionNames = {copy, caesar, rail, mono, vigen, greek};
        encryptionType = new JComboBox<String>(encryptionNames);

        // set up buttons
        encrypt = new JButton("Encrypt");
        //encrypt.addActionListener(this);
        encrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                if (!MessagePanel.this.clientContext.user.hasCurrent()) {
                    JOptionPane.showMessageDialog(MessagePanel.this, "You are not signed in.");
                } else if (!MessagePanel.this.clientContext.conversation.hasCurrent()) {
                    JOptionPane.showMessageDialog(MessagePanel.this, "You must select a conversation.");
                } else {
                    String var2 = (String) JOptionPane.showInputDialog(MessagePanel.this, "Enter message:", "Add Message", -1, (Icon) null, (Object[]) null, "");
                    if (var2 != null && var2.length() > 0) {
                        MessagePanel.this.clientContext.message.addMessage(MessagePanel.this.clientContext.user.getCurrent().id, MessagePanel.this.clientContext.conversation.getCurrentId(), var2);
                        MessagePanel.this.getAllMessages(MessagePanel.this.clientContext.conversation.getCurrent());
                    }
                }

            }
        });
        decrypt = new JButton("Decrypt");
        //decrypt.addActionListener(this);

        JPanel buttons = new JPanel(new FlowLayout());

        buttons.add(encrypt);
        buttons.add(decrypt);
        buttons.add(encryptionType);

        p.add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Add the text areas to the GUI
     */
    private void addTextAreas(JPanel p) {
        // prepare the labels
        JLabel plainLabel = new JLabel("Plain text");
        JLabel cipherLabel = new JLabel("Cipher text");
        plainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cipherLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        setTextAreas();

        JPanel textAreas = new JPanel();
        textAreas.setLayout(new BoxLayout(textAreas, BoxLayout.Y_AXIS));

        textAreas.add(plainLabel);
        textAreas.add(plain);
        textAreas.add(cipherLabel);
        textAreas.add(cipher);

        p.add(textAreas, BorderLayout.CENTER);
    }

    /**
     * Set up the text areas
     */
    private void setTextAreas() {
        // Set up default display messages
        plainText = new String("Please enter plain text here.");
        cipherText = new String("Please enter cipher text here.");

        plain = new JTextArea(plainText, getWidth() / 2, getHeight() / 2);
        cipher = new JTextArea(cipherText, getWidth() / 2, getHeight() / 2);

        plain.setLineWrap(true);
        cipher.setLineWrap(true);

        plain.setWrapStyleWord(true);
        cipher.setWrapStyleWord(true);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        plain.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        cipher.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    }


    public MessagePanel(ClientContext var1) {
        super(new GridBagLayout());
        this.clientContext = var1;
        this.initialize();
        JPanel messageP = new JPanel(new BorderLayout());
        addTextAreas(messageP);
        addButtons(messageP);
        this.add(messageP);
    }

    private void initialize() {
        JPanel var1 = new JPanel(new GridBagLayout());
        GridBagConstraints var2 = new GridBagConstraints();
        JPanel var3 = new JPanel(new FlowLayout(0));
        GridBagConstraints var4 = new GridBagConstraints();
        var4.gridx = 0;
        var4.gridy = 0;
        var4.anchor = 19;
        JPanel var5 = new JPanel(new FlowLayout(0));
        GridBagConstraints var6 = new GridBagConstraints();
        var6.gridx = 0;
        var6.gridy = 1;
        var6.anchor = 19;
        this.messageConversationLabel.setAlignmentX(0.0F);
        var3.add(this.messageConversationLabel);
        this.messageOwnerLabel.setAlignmentX(0.0F);
        var5.add(this.messageOwnerLabel);
        var1.add(var3, var4);
        var1.add(var5, var6);
        var1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel var7 = new JPanel();
        GridBagConstraints var8 = new GridBagConstraints();
        JList var9 = new JList(this.messageListModel);
        var9.setSelectionMode(0);
        var9.setVisibleRowCount(15);
        var9.setSelectedIndex(-1);
        JScrollPane var10 = new JScrollPane(var9);
        var7.add(var10);
        var10.setMinimumSize(new Dimension(500, 200));
        var10.setPreferredSize(new Dimension(500, 200));
        JPanel var11 = new JPanel();
        GridBagConstraints var12 = new GridBagConstraints();
        //JButton var13 = new JButton("Add");
        //var11.add(var13);
        var2.gridx = 0;
        var2.gridy = 0;
        var2.gridwidth = 10;
        var2.gridheight = 1;
        var2.fill = 2;
        var2.anchor = 23;
        var8.gridx = 0;
        var8.gridy = 1;
        var8.gridwidth = 10;
        var8.gridheight = 8;
        var8.fill = 1;
        var8.anchor = 23;
        var8.weighty = 0.8D;
        var12.gridx = 0;
        var12.gridy = 11;
        var12.gridwidth = 10;
        var12.gridheight = 1;
        var12.fill = 2;
        var12.anchor = 23;
        this.add(var1, var2);
        this.add(var7, var8);
        this.add(var11, var12);

        this.getAllMessages(this.clientContext.conversation.getCurrent());
    }

    // External agent calls this to trigger an update of this panel's contents.
    public void update(ConversationSummary owningConversation) {

        final User u = (owningConversation == null) ?
                null :
                clientContext.user.lookup(owningConversation.owner);

        messageOwnerLabel.setText("Owner: " +
                ((u == null) ?
                        ((owningConversation == null) ? "" : owningConversation.owner) :
                        u.name));

        messageConversationLabel.setText("Conversation: " + owningConversation.title);

        getAllMessages(owningConversation);
    }


    // Populate ListModel
    // TODO: don't refetch messages if current conversation not changed
    private void getAllMessages(ConversationSummary conversation) {
        messageListModel.clear();

        for (final Message m : clientContext.message.getConversationContents(conversation)) {
            // Display author name if available.  Otherwise display the author UUID.
            final String authorName = clientContext.user.getName(m.author);

            final String displayString = String.format("%s: [%s]: %s",
                    ((authorName == null) ? m.author : authorName), m.creation, m.content);

            messageListModel.addElement(displayString);
        }
    }
}
