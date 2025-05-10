import random
from datetime import datetime, timedelta

descriptions = ["Coffee In Airport", "Boating", "Taxi", "Lunch", "Hotel Stay", "Flight", "Conference", "Train Ticket"]
currencies = ["USD", "EUR", "INR"]
base_date = datetime(2020, 1, 1)

def random_date():
    return (base_date + timedelta(days=random.randint(0, 1500))).strftime("%B %d, %Y")

def generate_expense_entry():
    desc = random.choice(descriptions)
    curr = random.choice(currencies)
    amount = round(random.uniform(5, 500), 2)
    date = random_date()
    return f"""    <expense>
        <description> {desc} </description>
        <currencytype> {curr} </currencytype>
        <amount> {amount} </amount>
        <date> {date} </date>
    </expense>\n"""

def generate_large_xml(file_path, entry_count):
    with open(file_path, "w", encoding="utf-8") as f:
        f.write("<expenses>\n")
        for _ in range(entry_count):
            f.write(generate_expense_entry())
        f.write("</expenses>\n")

if __name__ == "__main__":
    # Change 10_000_000 to increase or decrease file size (~2–3 GB for that count)
    generate_large_xml("large_expenses.xml", entry_count=100)
    print("✅ large_expenses.xml generated successfully.")
