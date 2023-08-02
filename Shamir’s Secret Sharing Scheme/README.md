# Shamir Secret Sharing

This project implements the Shamir Secret Sharing algorithm, a cryptographic algorithm that allows a secret to be split into multiple "shares" and distributed among a group of participants. The secret can only be reconstructed by a subset of those participants, known as the threshold.

## Requirements

To run this program, you will need:
* Java("19.0.1") or higher.

## Installation

>1. Clone this repository or download the source code.
>2. Navigate to the project directory.
>3. Open CMD and navigate to the downloaded folder.
>4. Write the secret code in a .txt with name 'file.txt'.
>5. Just type javac file_name.java
>6. You will see that a new file will be created in the same folder with extension .class.
>7. Now again open CMD and just type java file_name i.e. the file that was recently created with extension .class.
>8. Hit enter and you will see the output in the window.

## Usage
* To use the Shamir Secret Sharing algorithm in your own Java code, open the shamir.java file:
>1. Write your secret message in file.txt file.
>2. Inside code:
>>1. N is the total number of shares to be created.
>>2. K is the number of shares required to reconstruct the secret.
>>3. S is the secret to be shared.
>3. Details.txt file created is the file containing all the shares and all the reconstructed data.
>4. Plaintext.txt file created is the file containing the plaintext of the secret message.
