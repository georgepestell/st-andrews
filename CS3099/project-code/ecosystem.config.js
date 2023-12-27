module.exports = {
    apps : [{
      name: "fpf-backend",
      script: "./backend/src/app.js",
      env: {
        NODE_ENV: "development",
      },
      env_production: {
        NODE_ENV: "production",
      },
      out_file: "./logs/backend.log",
      log_date_format: 'YYYY-MM-DD HH:mm',
      combine_logs: true
    }]
  }