package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"strings"

	"github.com/aws/aws-lambda-go/lambda"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/service/s3"
)

type Order struct {
	OrderID string  `json:"order_id"`
	Amount  float64 `json:"amount"`
	Item    string  `json:"item"`
}

var (
	s3Client *s3.Client
)

func init() {
	// Initialize the S3 client outside of the handler, during the init phase
	cfg, err := config.LoadDefaultConfig(context.TODO())
	if err != nil {
		log.Fatalf("unable to load SDK config, %v", err)
	}

	s3Client = s3.NewFromConfig(cfg)
}

func uploadReceiptToS3(ctx context.Context, bucketName, key, receiptContent string) error {
	_, err := s3Client.PutObject(ctx, &s3.PutObjectInput{
		Bucket: &bucketName,
		Key:    &key,
		Body:   strings.NewReader(receiptContent),
	})
	if err != nil {
		log.Printf("Failed to upload receipt to S3: %v", err)
		return err
	}
	return nil
}

func handleRequest(ctx context.Context, event json.RawMessage) error {
	// Parse the input event
	var order Order
	if err := json.Unmarshal(event, &order); err != nil {
		log.Printf("Failed to unmarshal event: %v", err)
		return err
	}

	// Access environment variables
	bucketName := os.Getenv("RECEIPT_BUCKET")
	if bucketName == "" {
		log.Printf("RECEIPT_BUCKET environment variable is not set")
		return fmt.Errorf("missing required environment variable RECEIPT_BUCKET")
	}

	// Create the receipt content and key destination
	receiptContent := fmt.Sprintf("OrderID: %s\nAmount: $%.2f\nItem: %s",
		order.OrderID, order.Amount, order.Item)
	key := "receipts/" + order.OrderID + ".txt"

	// Upload the receipt to S3 using the helper method
	if err := uploadReceiptToS3(ctx, bucketName, key, receiptContent); err != nil {
		return err
	}

	log.Printf("Successfully processed order %s and stored receipt in S3 bucket %s", order.OrderID, bucketName)
	return nil
}

func main() {
	lambda.Start(handleRequest)
}
