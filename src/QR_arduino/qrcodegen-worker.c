
#define _CRT_SECURE_NO_WARNINGS
#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "qrcodegen.h"

//잠시주석처리
/*
int main(void) {
	while (true) {

		// Read data length or exit
		int length;
		if (scanf("%d", &length) != 1)
			return EXIT_FAILURE;
		if (length == -1)
			break;

		// Read data bytes
		bool isAscii = true;
		uint8_t *data = malloc(length * sizeof(uint8_t));
		if (data == NULL) {
			perror("malloc");
			return EXIT_FAILURE;
		}
		for (int i = 0; i < length; i++) {
			int b;
			if (scanf("%d", &b) != 1)
				return EXIT_FAILURE;
			data[i] = (uint8_t)b;
			isAscii &= 0 < b && b < 128;
		}

		// Read encoding parameters
		int errCorLvl, minVersion, maxVersion, mask, boostEcl;
		if (scanf("%d %d %d %d %d", &errCorLvl, &minVersion, &maxVersion, &mask, &boostEcl) != 5)
			return EXIT_FAILURE;

		// Allocate memory for QR Code
		int bufferLen = qrcodegen_BUFFER_LEN_FOR_VERSION(maxVersion);
		uint8_t *qrcode = malloc(bufferLen * sizeof(uint8_t));
		uint8_t *tempBuffer = malloc(bufferLen * sizeof(uint8_t));
		if (qrcode == NULL || tempBuffer == NULL) {
			perror("malloc");
			return EXIT_FAILURE;
		}

		// Try to make QR Code symbol
		bool ok;
		if (isAscii) {
			char *text = malloc((length + 1) * sizeof(char));
			if (text == NULL) {
				perror("malloc");
				return EXIT_FAILURE;
			}
			for (int i = 0; i < length; i++)
				text[i] = (char)data[i];
			text[length] = '\0';
			ok = qrcodegen_encodeText(text, tempBuffer, qrcode, (enum qrcodegen_Ecc)errCorLvl,
				minVersion, maxVersion, (enum qrcodegen_Mask)mask, boostEcl == 1);
			free(text);
		}
		else if (length <= bufferLen) {
			memcpy(tempBuffer, data, length * sizeof(data[0]));
			ok = qrcodegen_encodeBinary(tempBuffer, (size_t)length, qrcode, (enum qrcodegen_Ecc)errCorLvl,
				minVersion, maxVersion, (enum qrcodegen_Mask)mask, boostEcl == 1);
		}
		else
			ok = false;
		free(data);
		free(tempBuffer);

		if (ok) {
			// Print grid of modules
			int size = qrcodegen_getSize(qrcode);
			printf("%d\n", (size - 17) / 4);
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++)
					printf("%d\n", qrcodegen_getModule(qrcode, x, y) ? 1 : 0);
			}
		}
		else
			printf("-1\n");
		free(qrcode);
		fflush(stdout);
	}
	return EXIT_SUCCESS;
}
*/